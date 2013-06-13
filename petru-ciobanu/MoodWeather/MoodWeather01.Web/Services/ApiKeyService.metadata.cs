
namespace MoodWeather.Web.SqlModel
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.DataAnnotations;
    using System.Linq;
    using System.ServiceModel.DomainServices.Hosting;
    using System.ServiceModel.DomainServices.Server;


    // The MetadataTypeAttribute identifies WeatherSetupMetadata as the class
    // that carries additional metadata for the WeatherSetup class.
    [MetadataTypeAttribute(typeof(WeatherSetup.WeatherSetupMetadata))]
    public partial class WeatherSetup
    {

        // This class allows you to attach custom attributes to properties
        // of the WeatherSetup class.
        //
        // For example, the following marks the Xyz property as a
        // required property and specifies the format for valid values:
        //    [Required]
        //    [RegularExpression("[A-Z][A-Za-z0-9]*")]
        //    [StringLength(32)]
        //    public string Xyz { get; set; }
        internal sealed class WeatherSetupMetadata
        {

            // Metadata classes are not meant to be instantiated.
            private WeatherSetupMetadata()
            {
            }

            public Nullable<byte> active { get; set; }

            public string api_calls { get; set; }

            public string api_key { get; set; }

            public string api_url { get; set; }

            public string apic_call_minute { get; set; }

            public int id { get; set; }
        }
    }
}
