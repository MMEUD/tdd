using System.ServiceModel;
using System.ServiceModel.Channels;
using System.ServiceModel.Web;

namespace MoodWeather.Service.Web
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "ICrossDomainService" in both code and config file together.
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "ICrossDomainService" in both code and config file together.
    [ServiceContract]
    public interface ICrossDomainService
    {
        [OperationContract]
        [WebGet(UriTemplate = "ClientAccessPolicy.xml")]
        Message ProvidePolicyFile();
    }
}
