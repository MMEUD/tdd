
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
    public class ApiKeyService : LinqToEntitiesDomainService<ApiListEntities>
    {

        // TODO:
        // Consider constraining the results of your query method.  If you need additional input you can
        // add parameters to this method or create additional query methods with different names.
        // To support paging you will need to add ordering to the 'WeatherSetups' query.
        public IQueryable<WeatherSetup> GetWeatherSetups()
        {
            return this.ObjectContext.WeatherSetups;
        }

        public void InsertWeatherSetup(WeatherSetup weatherSetup)
        {
            if ((weatherSetup.EntityState != EntityState.Detached))
            {
                this.ObjectContext.ObjectStateManager.ChangeObjectState(weatherSetup, EntityState.Added);
            }
            else
            {
                this.ObjectContext.WeatherSetups.AddObject(weatherSetup);
            }
        }

        public void UpdateWeatherSetup(WeatherSetup currentWeatherSetup)
        {
            this.ObjectContext.WeatherSetups.AttachAsModified(currentWeatherSetup, this.ChangeSet.GetOriginal(currentWeatherSetup));
        }

        public void DeleteWeatherSetup(WeatherSetup weatherSetup)
        {
            if ((weatherSetup.EntityState == EntityState.Detached))
            {
                this.ObjectContext.WeatherSetups.Attach(weatherSetup);
            }
            this.ObjectContext.WeatherSetups.DeleteObject(weatherSetup);
        }
    }
}


