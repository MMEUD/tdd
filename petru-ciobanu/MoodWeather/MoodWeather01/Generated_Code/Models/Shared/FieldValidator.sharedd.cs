using System;
using System.Collections.Generic;
using System.Linq;

using System.ComponentModel.DataAnnotations;
namespace MoodWeather01.Web.Models.Shared
{
    public class FieldValidator
    {
        public static ValidationResult IsEmailValid(string Email, ValidationContext context)
        {
            if (Email.Length > 0)
            {
                return ValidationResult.Success;
            }
            else
            {
                return new ValidationResult("The Email Address Is Invalid ", new string[] { "Email" });
            }
        }
    }
}