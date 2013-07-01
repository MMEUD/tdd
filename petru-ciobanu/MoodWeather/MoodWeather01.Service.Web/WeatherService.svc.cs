using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.ServiceModel;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace MoodWeather.Service.Web
{
    [ServiceBehavior]
    // [ServiceBehavior(InstanceContextMode = InstanceContextMode.PerSession)]
    public class WeatherService : IWeatherService
    {
        private readonly SqlConnection _cn = new SqlConnection(ConfigurationManager.ConnectionStrings["CityListConnectionString"].ConnectionString);
        private string _apiKey;
        private string _apiUrl;
        private string _commandType;
        private int _countCity;
        private DataTable _dataTable;
        private SqlDataAdapter _sqlAdapter;
        private string _ftpPassword;
        private string _ftpServerIp;
        private string _ftpUserName;


        public string CheckCity(string cityName, string countryCode)
        {
            string response;
            _cn.Open();
            const string sql = "SELECT * FROM WeatherSetup where active=1 ORDER BY id";
            using (var cmd = new SqlCommand(sql, _cn))
            {
                cmd.CommandType = System.Data.CommandType.Text;
                SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                while (rdr.Read())
                {
                    _apiKey = rdr["api_key"].ToString();
                    _apiUrl = rdr["api_url"].ToString();
                }

                _cn.Close();
            }


            var webClient = new WebClient();
            var docs = XDocument.Load(_apiUrl + _apiKey + "/astronomy/forecast/q/" + countryCode + "/" + cityName + "/" + ".xml");
            var query = docs.Descendants("simpleforecast").Elements("forecastdays").Elements("forecastday");
            if (query.Any() == true)
            {
                response = "Server Response :" + "\n" + "Day :" + query.Elements("date").Elements("weekday").ElementAt(0).Value + "\n"+ "Condition :" +query.Elements("conditions").ElementAt(0).Value.ToString(); 
                
            }
            else
            {
                response = "No cities match your search query";
            }

            return response.ToUpper();
        }

        public ObservableCollection<CityList> SelectCity()
        {
            var listCity = new ObservableCollection<CityList>();
            try
            {
                _cn.Open();
                const string sql = "SELECT * FROM CityList  WHERE Active=1  ORDER BY CityName";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    while (rdr.Read())
                    {
                        listCity.Add(new CityList
                        {
                            CityName = rdr["CityName"].ToString(),
                            CityId = rdr["Id"].ToString(),
                            CityAlternative = rdr["CityAlternative"].ToString(),
                            CountryCode = rdr["CountryCode"].ToString(),
                            CountryCodeTeamco = rdr["CountryCodeTeamco"].ToString(),
                        });
                    }

                    _cn.Close();
                }
            }
            catch (FaultException faultEx)
            {
                WriteToLogError("WeatherServiceIIS" + "SelectCitySQL" + faultEx.Message);
            }
            return listCity;
        }



        //GET ALL  CITY FROM CITY TABLE
        public void GetCityLists()
        {
            try
            {
                _cn.Open();
                const string sql = "SELECT CityName,CityId,CityAlternative,CountryCode,CountryCodeTeamco FROM CityList WHERE Active=1 ORDER BY CityName ";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    _sqlAdapter = new SqlDataAdapter(cmd);
                    _dataTable = new DataTable("CityListData");
                    _sqlAdapter.Fill(_dataTable);
                    _countCity = _dataTable.Rows.Count;
                    _cn.Close();
                }
            }
            catch (FaultException faultEx)
            {
                WriteToLogError("WeatherServiceIIS" + "GetCityListsSQL" + faultEx.Message);
            }
        }



        //SEND  REQUEST TO  WEATHER SERVICE CLIENT ACTION
        public void GetWeatherForecast(string countryCode, string countryCodeTeamco, string city, string cityalternative, string serviceType,string cityId)
        {
            _cn.Open();
            const string sql = "SELECT * FROM WeatherSetup where active=1 ORDER BY id";
            using (var cmd = new SqlCommand(sql, _cn))
            {
                cmd.CommandType = System.Data.CommandType.Text;
                SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                while (rdr.Read())
                {
                    _apiKey = rdr["api_key"].ToString();
                    _apiUrl = rdr["api_url"].ToString();
                }

                _cn.Close();
            }

            try
            {
                string sunset = null;
                string sunrise = null;
              
                var docs = XDocument.Load(_apiUrl + _apiKey + "/astronomy/forecast/q/" + countryCode + "/" + cityalternative + "/" + ".xml");
                var query = docs.Descendants("simpleforecast").Elements("forecastdays").Elements("forecastday");
                var moonphase = docs.Descendants("moon_phase");

                foreach (var res in moonphase)
                {
                    sunset = res.Element("sunset").Element("hour").Value + ":" + res.Element("sunset").Element("minute").Value;
                    sunrise = res.Element("sunrise").Element("hour").Value + ":" + res.Element("sunrise").Element("minute").Value;
                }

                var weatherCondition = new ObservableCollection<WeatherCondition>();
                DateTime forecastDate;
                foreach (var result in query)
                {
                    forecastDate = new DateTime(
                        Convert.ToInt16(result.Element("date").Element("year").Value),
                        Convert.ToInt16(result.Element("date").Element("month").Value),
                        Convert.ToInt16(result.Element("date").Element("day").Value));
                    InsertData(city, cityId,countryCodeTeamco, result.Element("conditions").Value, sunrise, sunset, result.Element("low").Element("celsius").Value,
                               result.Element("high").Element("celsius").Value, result.Element("icon").Value, result.Element("skyicon").Value,
                               forecastDate, result.Element("date").Element("weekday").Value, serviceType);
                }
            }
            catch (Exception ex)
            {
                WriteToLogError("WeatherServiceIIS" + "GetWeatherForecast" + ex.Message.ToString());
            }
        }



        //INSERT WEATHER CONDITION TO  SQL
        public void InsertData(string cityName, string cityId,string countryCode, string condition, string sunrise, string sunset, string tempLow, string tempHigh, string icon, string skyIcon, DateTime date, string weekDay, string updateBy)
        {
            try
            {
                _cn.Close();
                _cn.Open();
                const string sql = "sp_CityWeather_InsertWeather";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.StoredProcedure;
                    cmd.Parameters.Add(new SqlParameter("@CityName", cityName));
                    cmd.Parameters.Add(new SqlParameter("@CityId", cityId));
                    cmd.Parameters.Add(new SqlParameter("@CountryCode", countryCode));
                    cmd.Parameters.Add(new SqlParameter("@Condition", condition));
                    cmd.Parameters.Add(new SqlParameter("@Sunrise", sunrise));
                    cmd.Parameters.Add(new SqlParameter("@Sunset", sunset));
                    cmd.Parameters.Add(new SqlParameter("@TempLow", tempLow));
                    cmd.Parameters.Add(new SqlParameter("@TempHigh", tempHigh));
                    cmd.Parameters.Add(new SqlParameter("@Icon", icon));
                    cmd.Parameters.Add(new SqlParameter("@SkyIcon", skyIcon));
                    cmd.Parameters.Add(new SqlParameter("@Date", date));
                    cmd.Parameters.Add(new SqlParameter("@WeekDay", weekDay));
                    cmd.Parameters.Add(new SqlParameter("@LastUpdate", System.DateTime.Now));
                    cmd.Parameters.Add(new SqlParameter("@UpdateBy", updateBy));
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    _cn.Close();
                }
            }
            catch (SqlException ex)
            {
                WriteToLogError("WeatherServiceIIS" + "InsertDataSQL" + ex.Message.ToString());
            }
        }





       

        public void CreateXmlBackup(string fileName, string content)
        {
            string nowStr = DateTime.Now.ToShortDateString(); 
            try
            {
                string template = AppDomain.CurrentDomain.BaseDirectory + "/App_Data/" + "weather_forecast.xml";
                string destFileName = AppDomain.CurrentDomain.BaseDirectory + "/App_Data/" + String.Format("{0:yyyyMMdd}", Convert.ToDateTime(nowStr))+".xml";
                var fi = new FileInfo(template);
                if (fi.Exists)
                {
                    fi.CopyTo(destFileName,true);
                }
            }
            catch (FaultException faultEx)
            {
                WriteToLogError("WeatherServiceIIS" + faultEx.ToString());
            }
        }

        public void DeleteDatabase()
        {
            try
            {
                _cn.Open();
                const string sql = "DELETE  FROM CityWeather";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    cmd.StatementCompleted += new StatementCompletedEventHandler(cmd_StatementCompleted);
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    _cn.Close();
                }
            }
            catch (SqlException e)
            {
                WriteToLogError("WeatherServiceIIS" + "DeleteDataBaseSQL" + e.Errors.ToString());
            }
        }


        public void cmd_StatementCompleted(object sender, StatementCompletedEventArgs e)
        {
        }



        //RETURN CITY  CONDITION TO USERS
        public ObservableCollection<WeatherCondition> GetCityCondition()
        {
            var weatherCondition = new ObservableCollection<WeatherCondition>();
            try
            {
                _cn.Open();
                const string sql = "SELECT * FROM CityWeather ORDER BY CityName";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    while (rdr.Read())
                    {
                        weatherCondition.Add(new WeatherCondition
                        {
                            Id = Convert.ToInt32(rdr["Id"]),
                            CityName = rdr["CityName"].ToString(),
                            CountryCode = rdr["CountryCode"].ToString(),
                            Condition = rdr["Condition"].ToString(),
                            Sunrise = rdr["Sunrise"].ToString(),
                            Sunset = rdr["Sunset"].ToString(),
                            TempLow = rdr["TempLow"].ToString(),
                            TempHigh = rdr["TempHigh"].ToString(),
                            Icon = rdr["Icon"].ToString(),
                            SkyIcon = rdr["SkyIcon"].ToString(),
                            Date = Convert.ToDateTime(rdr["Date"].ToString()),
                            WeekDay = rdr["WeekDay"].ToString(),
                            LastUpdate = Convert.ToDateTime(rdr["LastUpdate"].ToString()),
                            UpdateBy = rdr["UpdateBy"].ToString()
                        });
                    }
                    _cn.Close();
                }
            }
            catch (SqlException ex)
            {
                WriteToLogError("WeatherServiceIIS" + "GetCityConditionSql" + ex.Message.ToString());
            }
            return weatherCondition;
        }

       
        public void SaveXmlFile()
        {
            try
            {
                var weatherCondition = new ObservableCollection<WeatherCondition>();
                _cn.Open();
                const string sql = "SELECT * FROM  CityWeather ";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    while (rdr.Read())
                    {
                        weatherCondition.Add(new WeatherCondition
                        {
                            Id = Convert.ToInt32(rdr["Id"]),
                            CityName = rdr["CityName"].ToString(),
                            CountryCode = rdr["CountryCode"].ToString(),
                            Condition = rdr["Condition"].ToString(),
                            Sunrise = rdr["Sunrise"].ToString(),
                            Sunset = rdr["Sunset"].ToString(),
                            TempLow = rdr["TempLow"].ToString(),
                            TempHigh = rdr["TempHigh"].ToString(),
                            Icon = rdr["Icon"].ToString(),
                            SkyIcon = rdr["SkyIcon"].ToString(),
                            Date = Convert.ToDateTime(rdr["Date"].ToString()),
                            WeekDay = rdr["WeekDay"].ToString(),
                            LastUpdate = Convert.ToDateTime(rdr["LastUpdate"].ToString()),
                            UpdateBy = rdr["UpdateBy"].ToString()
                        });
                    }
                    _cn.Close();
                    GenerateXmlData(weatherCondition);
                }
            }
            catch (XmlException faultEx)
            {
                WriteToLogError("WeatherServiceIIS" + "SaveXmlSql:" + faultEx);
            }
        }

       
        //RETURN CITY  CONDITION TO FIRST PAGE
        public ObservableCollection<WeatherCondition> SelectCityCondition(string cityName)
        {
            var weatherCondition = new ObservableCollection<WeatherCondition>();
            try
            {
                _cn.Open();
                string sql = "SELECT * FROM CityWeather WHERE (CityName ='" + cityName + "')";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    while (rdr.Read())
                    {
                        weatherCondition.Add(new WeatherCondition
                        {
                            Id = Convert.ToInt32(rdr["Id"]),
                            CityName = rdr["CityName"].ToString(),
                            CountryCode = rdr["CountryCode"].ToString(),
                            Condition = rdr["Condition"].ToString(),
                            Sunrise = rdr["Sunrise"].ToString(),
                            Sunset = rdr["Sunset"].ToString(),
                            TempLow = rdr["TempLow"].ToString(),
                            TempHigh = rdr["TempHigh"].ToString(),
                            Icon = rdr["Icon"].ToString(),
                            SkyIcon = rdr["SkyIcon"].ToString(),
                            Date = Convert.ToDateTime(rdr["Date"].ToString()),
                            WeekDay = rdr["WeekDay"].ToString(),
                            LastUpdate = Convert.ToDateTime(rdr["LastUpdate"].ToString()),
                            UpdateBy = rdr["UpdateBy"].ToString()
                        });
                    }
                    _cn.Close();
                }
            }
            catch (SqlException ex)
            {
                WriteToLogError("WeatherServiceIIS" + "SelectCityConditionSql" + ex.Message.ToString());
            }
            return weatherCondition;
        }

        //START SERVICE FOR GET DATA
        public void StartDataCollect(string command)
        {
            try
            {
                _commandType = command;
                if (command == "userCommand")
                {
                    _commandType = "userCommand";
                }
                else
                {
                    _commandType = "service";
                   //  DeleteDatabase();
                }
            }
            catch (Exception ex)
            {
                WriteToLogError("WeatherServiceIIS" + "StartDataCollect" + ex.Message.ToString());
            }
        }
      
        
        public String UploadFileFtpAutomate(string raspuns)
        {
            try
            {
                _cn.Open();
                const string sql = "SELECT * FROM WeatherPublish WHERE ftp_active=1 ORDER BY id";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = System.Data.CommandType.Text;
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    while (rdr.Read())
                    {
                        _ftpServerIp = rdr["ftp_address"].ToString();
                        _ftpUserName = rdr["ftp_user_name"].ToString();
                        _ftpPassword = rdr["ftp_user_password"].ToString();
                    }

                    if (rdr.FieldCount > 0)
                    {
                        raspuns = FtpUpload("raspuns");
                    }
                    else
                    {
                        WriteToLogError("WeatherServiceIIS" + "UploadXmlFileToFtp:" + "NoActiveFtpServerFound!");
                    }
                    _cn.Close();
                }
            }
            catch (Exception ex)
            {
                WriteToLogError("WeatherServiceIIS" + "UploadXmlFileToFtp:" + ex.Message);
            }

            return raspuns;
        }

        public void WriteToLog(string msg)
        {
            EventLog.WriteEntry("WeatherServiceIIS", msg, EventLogEntryType.Information, 888);
        }

        public void WriteToLogError(string msg)
        {
            EventLog.WriteEntry("WeatherServiceIIS", msg, EventLogEntryType.Error, 666);
        }

        private String FtpUpload(string raspuns)
        {
            string filename = AppDomain.CurrentDomain.BaseDirectory + "/App_Data/weather_forecast.xml";
            FileInfo objFile = new FileInfo(filename);
            FtpWebRequest objFTPRequest;
            objFTPRequest = (FtpWebRequest)FtpWebRequest.Create(new Uri("ftp://" + _ftpServerIp + "/" + objFile.Name));
            objFTPRequest.Credentials = new NetworkCredential(_ftpUserName, _ftpPassword);
            objFTPRequest.KeepAlive = false;
            objFTPRequest.UseBinary = true;
            objFTPRequest.ContentLength = objFile.Length;
            objFTPRequest.Method = WebRequestMethods.Ftp.UploadFile;
            int intBufferLength = 16 * 1024;
            byte[] objBuffer = new byte[intBufferLength];
            FileStream objFileStream = objFile.OpenRead();
            try
            {
                // Get Stream of the file
                Stream objStream = objFTPRequest.GetRequestStream();
                int len = 0;
                while ((len = objFileStream.Read(objBuffer, 0, intBufferLength)) != 0)
                {
                    objStream.Write(objBuffer, 0, len);
                }
                objStream.Close();
                objFileStream.Close();
                raspuns = "226 Transfer OK";
            }
            catch (Exception ex)
            {
                raspuns = ex.ToString();
                WriteToLogError("WeatherServiceIIS" + "UploadXmlFileToFtp:" + ex.Message);
            }

            return raspuns;
        }

        private void GenerateXmlData(ObservableCollection<WeatherCondition> res)
        {
            var dateSelect = new List<WeatherCondition>(res);
            var citySelect = new List<WeatherCondition>(res);
            var queryDate = from p in dateSelect
                            group p by p.Date into dateCount
                            select new { date = dateCount.Key, Count = dateCount.Count() };
            var queryCity = from ds in queryDate
                            join ws in citySelect on ds.date equals ws.Date into cs
                            select new { Key = ds.date, Items = cs };
            var settings = new XmlWriterSettings { Indent = true };
            try
            {
                using (XmlWriter writer = XmlWriter.Create(AppDomain.CurrentDomain.BaseDirectory + "/App_Data/" + "weather_forecast.xml", settings))
                {
                    writer.WriteStartDocument();
                    writer.WriteStartElement("WEATHER");
                    writer.WriteStartElement("ALERT");
                    writer.WriteString("");
                    writer.WriteEndElement();
                    foreach (var item in queryCity)
                    {
                        writer.WriteStartElement("DATE");
                        writer.WriteAttributeString("YYYYMMDD", String.Format("{0:yyyyMMdd}", Convert.ToDateTime(item.Key.ToString())));
                        writer.WriteStartElement("SUNTIME");
                        writer.WriteStartElement("SUNRISE");
                        writer.WriteString(item.Items.ElementAt(0).Sunrise);
                        writer.WriteEndElement();//sunrise
                        writer.WriteStartElement("SUNSET");
                        writer.WriteString(item.Items.ElementAt(0).Sunset);
                        writer.WriteEndElement();//sunset
                        writer.WriteEndElement();//suntime

                        if (item.Items.ElementAt(0).TempLow.Length > 0)
                        {
                            writer.WriteStartElement("TIME");
                            writer.WriteAttributeString("HOUR", "08");
                            foreach (var element in item.Items)
                            {
                                writer.WriteStartElement("GEOGRAPHY");
                                if (element.CountryCode == "FR")
                                {
                                    writer.WriteAttributeString("AREA", element.CityName);
                                }
                                else
                                {
                                    writer.WriteAttributeString("AREA", element.CountryCode + "_" + element.CityName);
                                }
                                writer.WriteStartElement("ICON");
                                writer.WriteString(ChangeIcons(element.Icon) + ".png");
                                writer.WriteEndElement();//icons
                                writer.WriteStartElement("TEMPERATURE");
                                writer.WriteString(element.TempLow);
                                writer.WriteEndElement();//templow
                                writer.WriteStartElement("REMARK");
                                writer.WriteString("");
                                writer.WriteEndElement();
                                writer.WriteEndElement(); //geogra
                            }
                            writer.WriteEndElement();//time
                        }

                        if (item.Items.ElementAt(0).TempHigh.ToString().Length > 0)
                        {
                            writer.WriteStartElement("TIME");
                            writer.WriteAttributeString("HOUR", "14");
                            foreach (var element in item.Items)
                            {
                                writer.WriteStartElement("GEOGRAPHY");
                                if (element.CountryCode == "FR")
                                {
                                    writer.WriteAttributeString("AREA", element.CityName);
                                }
                                else
                                {
                                    writer.WriteAttributeString("AREA", element.CountryCode + "_" + element.CityName);
                                }
                                writer.WriteStartElement("ICON");
                                writer.WriteString(ChangeIcons(element.Icon )+ ".png");
                                writer.WriteEndElement();//icons
                                writer.WriteStartElement("TEMPERATURE");
                                writer.WriteString(element.TempHigh);
                                writer.WriteEndElement();//templow
                                writer.WriteStartElement("REMARK");
                                writer.WriteString("");
                                writer.WriteEndElement();
                                writer.WriteEndElement(); //geogra
                            }
                            writer.WriteEndElement();//time
                        }
                        writer.WriteStartElement("COMMENT");
                        writer.WriteString("");
                        writer.WriteEndElement();//COMMENT
                        writer.WriteEndElement();//DATE
                    }
                    writer.WriteEndElement(); // WEATHER
                    writer.WriteEndDocument();
                    writer.Flush();
                    writer.Close();
                    
                }

            }
            catch (Exception e)
            {
                WriteToLogError("WeatherServiceIIS" + "GenerateXmlFile:" + e.Message);
            }
            CreateXmlBackup("weather_copy.xml", "");
        }
    
        //CONVERT  WEATHER ICONS  TO  TEAMCO  ICONS 
        private String ChangeIcons(string iconName)
        {
            string newIconName=null;
            if (iconName == "chanceflurries" || iconName=="chancesnow" || iconName=="flurries" || iconName=="snow" )
            {
                newIconName = "snow";
            }
            if (iconName == "chancetstorms" || iconName == "tstorms" )
            {
                newIconName = "storm";
            }


            if (iconName == "chancerain")
            {
                newIconName = "rainclouds";
            }
            if ( iconName == "rain")
            {
                newIconName = "raincloudsmax";
            }

            if (iconName == "cloudy")
            {
                newIconName = "clouds";
            }

            if (iconName == "clear" || iconName == "sunny")
            {
                newIconName = "sun";
            }
            if (iconName == "mostlysunny" || iconName == "partlycloudy")
            {
                newIconName = "sunclouds";
            }

            if (iconName == "mostlycloudy" || iconName == "partlysunny")
            {
                newIconName = "suncloudsmax";
            }
            if (iconName == "sleet" || iconName == "chancesleet")
            {
                newIconName = "glaze";
            }
            if (iconName == "fog" || iconName=="hazy")
            {
                newIconName = "clouds";
            }

                return newIconName;
        }
    
    
    
    
    }
}