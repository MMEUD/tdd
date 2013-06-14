using System.ComponentModel.DataAnnotations;
using System.ServiceModel.DomainServices.Client;
using MoodWeather.Web.Resources;

namespace MoodWeather.Web
{
    public class ForgotInfo : Entity
    {
        private OperationBase currentOperation;
        private string emailAddress;


        [Key]
        [Required(ErrorMessageResourceName = "ValidationErrorRequiredField",
            ErrorMessageResourceType = typeof (ValidationErrorResources))]
        [Display(Order = 0, Name = "Email", Description = "Email Address For Registered User")]
        [RegularExpression(
            @"^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$",
            ErrorMessageResourceName = "ValidationErrorInvalidEmail",
            ErrorMessageResourceType = typeof (ValidationErrorResources))]
        // [StringLength(125, MinimumLength = 7, ErrorMessageResourceName = "ValidationErrorBadPasswordLength", ErrorMessageResourceType = typeof(ValidationErrorResources))]
        public string Email
        {
            get { return emailAddress; }

            set
            {
                if (emailAddress != value)
                {
                    ValidateProperty("Email", value);
                    emailAddress = value;
                    RaisePropertyChanged("Email");
                }
            }
        }


        /// <summary>
        ///     Gets or sets the current registration or login operation.
        /// </summary>
        internal OperationBase CurrentOperation
        {
            get { return currentOperation; }
            set
            {
                if (currentOperation != value)
                {
                    if (currentOperation != null)
                    {
                        currentOperation.Completed -= (s, e) => CurrentOperationChanged();
                    }

                    currentOperation = value;

                    if (currentOperation != null)
                    {
                        currentOperation.Completed += (s, e) => CurrentOperationChanged();
                    }

                    CurrentOperationChanged();
                }
            }
        }


        /// <summary>
        ///     Gets a value indicating whether the user is presently being registered or logged in.
        /// </summary>
        [Display(AutoGenerateField = false)]
        public bool IsRegistering
        {
            get { return CurrentOperation != null && !CurrentOperation.IsComplete; }
        }

        /// <summary>
        ///     Helper method for when the current operation changes.
        ///     Used to raise appropriate property change notifications.
        /// </summary>
        private void CurrentOperationChanged()
        {
            RaisePropertyChanged("IsRegistering");
        }
    }
}