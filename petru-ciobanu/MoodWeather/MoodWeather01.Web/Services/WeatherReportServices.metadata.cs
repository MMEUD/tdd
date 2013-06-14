
namespace MoodWeather.Web.SqlModel
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.DataAnnotations;
    using System.Linq;
    using System.ServiceModel.DomainServices.Hosting;
    using System.ServiceModel.DomainServices.Server;


    // The MetadataTypeAttribute identifies viewCityStatuMetadata as the class
    // that carries additional metadata for the viewCityStatu class.
    [MetadataTypeAttribute(typeof(viewCityStatu.viewCityStatuMetadata))]
    public partial class viewCityStatu
    {

        // This class allows you to attach custom attributes to properties
        // of the viewCityStatu class.
        //
        // For example, the following marks the Xyz property as a
        // required property and specifies the format for valid values:
        //    [Required]
        //    [RegularExpression("[A-Z][A-Za-z0-9]*")]
        //    [StringLength(32)]
        //    public string Xyz { get; set; }
        internal sealed class viewCityStatuMetadata
        {

            // Metadata classes are not meant to be instantiated.
            private viewCityStatuMetadata()
            {
            }

            public Nullable<byte> Active { get; set; }

            public string CityAlternative { get; set; }

            public Nullable<int> CityId { get; set; }

            public string CityName { get; set; }

            public string Country { get; set; }

            public string CountryCode { get; set; }

            public int Id { get; set; }

            public Nullable<DateTime> LastUpdate { get; set; }

            public string Teamco { get; set; }
        }
    }
}
