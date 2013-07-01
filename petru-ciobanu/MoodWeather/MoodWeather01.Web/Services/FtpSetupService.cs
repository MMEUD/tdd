
namespace MoodWeather.Web.Services
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.DataAnnotations;
    using System.Data;
    using System.Linq;
    using System.ServiceModel.DomainServices.EntityFramework;
    using System.ServiceModel.DomainServices.Hosting;
    using System.ServiceModel.DomainServices.Server;
    using MoodWeather.Web.SqlModel;


    // Implements application logic using the ApiListEntities context.
    // TODO: Add your application logic to these methods or in additional methods.
    // TODO: Wire up authentication (Windows/ASP.NET Forms) and uncomment the following to disable anonymous access
    // Also consider adding roles to restrict access as appropriate.
    // [RequiresAuthentication]
    [EnableClientAccess()]
    public class FtpSetupService : LinqToEntitiesDomainService<ApiListEntities>
    {

        // TODO:
        // Consider constraining the results of your query method.  If you need additional input you can
        // add parameters to this method or create additional query methods with different names.
        // To support paging you will need to add ordering to the 'WeatherPublishes' query.
        public IQueryable<WeatherPublish> GetWeatherPublishes()
        {
            return this.ObjectContext.WeatherPublishes;
        }

        public void InsertWeatherPublish(WeatherPublish weatherPublish)
        {
            if ((weatherPublish.EntityState != EntityState.Detached))
            {
                this.ObjectContext.ObjectStateManager.ChangeObjectState(weatherPublish, EntityState.Added);
            }
            else
            {
                this.ObjectContext.WeatherPublishes.AddObject(weatherPublish);
            }
        }

        public void UpdateWeatherPublish(WeatherPublish currentWeatherPublish)
        {
            this.ObjectContext.WeatherPublishes.AttachAsModified(currentWeatherPublish, this.ChangeSet.GetOriginal(currentWeatherPublish));
        }

        public void DeleteWeatherPublish(WeatherPublish weatherPublish)
        {
            if ((weatherPublish.EntityState == EntityState.Detached))
            {
                this.ObjectContext.WeatherPublishes.Attach(weatherPublish);
            }
            this.ObjectContext.WeatherPublishes.DeleteObject(weatherPublish);
        }
    }
}


