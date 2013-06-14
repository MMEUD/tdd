using System;
using System.IO;
using System.ServiceModel;

namespace MoodWeather.Service.Web
{
    
    [ServiceContract]
    public interface IUploadService
    {
        [OperationContract]
        String FileUpload(Stream fileStream); 
    }
}
