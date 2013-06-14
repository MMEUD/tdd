using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace MoodWeather.Service.Web
{
    public class UploadService : IUploadService
    {
       public String FileUpload(Stream sourceStream)
        {
            var filePath = AppDomain.CurrentDomain.BaseDirectory + "/App_Data/";
           const string fileName = "weather_forecast.xml";
                try
                {
                    FileStream targetStream = null;
                    using (targetStream = new FileStream(filePath + fileName , FileMode.Create, FileAccess.Write, FileShare.None))
                    {
                        const int bufferLen = 65000;
                        var buffer = new byte[bufferLen];
                        var count = 0;
                        while ((count = sourceStream.Read(buffer, 0, bufferLen)) > 0)
                        {
                            targetStream.Write(buffer, 0, count);

                        }
                        targetStream.Close();
                        sourceStream.Close();
                        return "FileSucces";
                    }
                }
                catch (Exception)
                {
                    return "Error";
                }
            }

        }
    }

