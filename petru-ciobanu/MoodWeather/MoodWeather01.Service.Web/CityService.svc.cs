using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.Data;
using System.Configuration;
using System.Data.SqlClient;
using System.Collections.ObjectModel;

namespace MoodWeather.Service.Web
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "CityService" in code, svc and config file together.

    public class CityService : ICityService
    {
        readonly SqlConnection _cn = new SqlConnection(ConfigurationManager.ConnectionStrings["CityListConnectionString"].ConnectionString);

        //RETURN CITY  LIST 
        public ObservableCollection<CityLists> GetCityList()
        {
            _cn.Open();
            var cityList = new ObservableCollection<CityLists>();
            const string sql = "SELECT * FROM CityList ORDER BY Id";
            using (var cmd = new SqlCommand(sql, _cn))
            {
                cmd.CommandType = CommandType.Text;
                var rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                while (rdr.Read())
                {
                    cityList.Add(new CityLists
                    {
                        Id = Convert.ToInt32(rdr["Id"]),
                        CityName = rdr["CityName"].ToString(),
                        CityAlternative = rdr["CityAlternative"].ToString(), 
                        Country = rdr["Country"].ToString(),
                        CountryCode = rdr["CountryCode"].ToString(),
                        CountryCodeTeamco = rdr["CountryCodeTeamco"].ToString(),
                        DateAdded = Convert.ToDateTime(rdr["DateAdded"].ToString()),
                        DateModified = Convert.ToDateTime(rdr["DateModified"].ToString()),
                        UserId = rdr["UserId"].ToString(),
                        Active=rdr["Active"].ToString()
                    });
                }

                _cn.Close();
            }
            return cityList;

        }



        //INSERT CITY
        public void InsertCity(string city,string cityalternative, string country, string countryCode,string countryCodeTeamco, string userId,string active)
        {
            try
            {
                _cn.Open();
                const string sql = "sp_CityList_InsertCity";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.Parameters.Add(new SqlParameter("@CityName", city));
                    cmd.Parameters.Add(new SqlParameter("@CityAlternative", cityalternative));
                    cmd.Parameters.Add(new SqlParameter("@Country", country));
                    cmd.Parameters.Add(new SqlParameter("@CountryCode", countryCode));
                    cmd.Parameters.Add(new SqlParameter("@CountryCodeTeamco", countryCodeTeamco));
                    cmd.Parameters.Add(new SqlParameter("@DateAdded", System.DateTime.Now));
                    cmd.Parameters.Add(new SqlParameter("@DateModified", System.DateTime.Now));
                    cmd.Parameters.Add(new SqlParameter("@UserId", userId));
                    cmd.Parameters.Add(new SqlParameter("@Active", active));
                    var rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    _cn.Close();
                }
            }
            catch (SqlException)
            {


            }
        }

        //DELETE CITY 
        public void DeleteCity(int id)
        {
            try
            {
                _cn.Open();
                const string sql = "sp_CityList_DeleteCity";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.Parameters.Add(new SqlParameter("@Original_Id", id));
                    var rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    _cn.Close();
                }
            }
            catch (SqlException)
            {


            }
        }
        //UPDATE CITY 
        public void UpdateCity(int id, string city,string cityalternative, string country, string countryCode,string countryCodeTeamco, string userId,string active)
        {
            try
            {
                _cn.Open();
                const string sql = "sp_CityList_UpdateCity";
                using (var cmd = new SqlCommand(sql, _cn))
                {
                    cmd.CommandType = CommandType.StoredProcedure;
                    cmd.Parameters.Add(new SqlParameter("@CityName", city));
                    cmd.Parameters.Add(new SqlParameter("@CityAlternative", cityalternative));
                    cmd.Parameters.Add(new SqlParameter("@Country", country));
                    cmd.Parameters.Add(new SqlParameter("@CountryCode", countryCode));
                    cmd.Parameters.Add(new SqlParameter("@CountryCodeTeamco", countryCodeTeamco));
                    cmd.Parameters.Add(new SqlParameter("@DateModified", System.DateTime.Now));
                    cmd.Parameters.Add(new SqlParameter("@UserId", userId));
                    cmd.Parameters.Add(new SqlParameter("@Original_Id", id));
                    cmd.Parameters.Add(new SqlParameter("@Id", id));
                    cmd.Parameters.Add(new SqlParameter("@Active", active));
                    SqlDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                    _cn.Close();
                }
            }
            catch (SqlException)
            {


            }
        }
    }
}
