using System;
using System.ComponentModel.DataAnnotations;
using System.ServiceModel.DomainServices.Client;
using System.ServiceModel.DomainServices.Client.ApplicationServices;
using MoodWeather.Web.Resources;

namespace MoodWeather.Web
{
    /// <summary>
    ///     This internal entity is used to ease the binding between the UI controls
    ///     (DataForm and the label displaying a validation error) and the log on
    ///     credentials entered by the user.
    /// </summary>
    public class LoginInfo : Entity
    {
        private LoginOperation currentLoginOperation;
        private bool rememberMe;
        private string userName;

        [Display(Name = "UserNameLabel", ResourceType = typeof (RegistrationDataResources))]
        [Required]
        public string UserName
        {
            get { return userName; }

            set
            {
                if (userName != value)
                {
                    ValidateProperty("UserName", value);
                    userName = value;
                    RaisePropertyChanged("UserName");
                }
            }
        }

        /// <summary>
        ///     Gets or sets a function that returns the password.
        /// </summary>
        internal Func<string> PasswordAccessor { get; set; }

        /// <summary>
        ///     Gets and sets the password.
        /// </summary>
        [Display(Name = "PasswordLabel", ResourceType = typeof (RegistrationDataResources))]
        [Required]
        public string Password
        {
            get { return (PasswordAccessor == null) ? string.Empty : PasswordAccessor(); }
            set
            {
                ValidateProperty("Password", value);

                // Do not store the password in a private field as it should
                // not be stored in memory in plain-text.  Instead, the supplied
                // PasswordAccessor serves as the backing store for the value.

                RaisePropertyChanged("Password");
            }
        }

        /// <summary>
        ///     Gets and sets the value indicating whether the user's authentication information
        ///     should be recorded for future logins.
        /// </summary>
        [Display(Name = "RememberMeLabel", ResourceType = typeof (ApplicationStrings))]
        public bool RememberMe
        {
            get { return rememberMe; }

            set
            {
                if (rememberMe != value)
                {
                    ValidateProperty("RememberMe", value);
                    rememberMe = value;
                    RaisePropertyChanged("RememberMe");
                }
            }
        }

        /// <summary>
        ///     Gets or sets the current login operation.
        /// </summary>
        internal LoginOperation CurrentLoginOperation
        {
            get { return currentLoginOperation; }
            set
            {
                if (currentLoginOperation != value)
                {
                    if (currentLoginOperation != null)
                    {
                        currentLoginOperation.Completed -= (s, e) => CurrentLoginOperationChanged();
                    }

                    currentLoginOperation = value;

                    if (currentLoginOperation != null)
                    {
                        currentLoginOperation.Completed += (s, e) => CurrentLoginOperationChanged();
                    }

                    CurrentLoginOperationChanged();
                }
            }
        }

        /// <summary>
        ///     Gets a value indicating whether the user is presently being logged in.
        /// </summary>
        [Display(AutoGenerateField = false)]
        public bool IsLoggingIn
        {
            get { return CurrentLoginOperation != null && !CurrentLoginOperation.IsComplete; }
        }

        /// <summary>
        ///     Gets a value indicating whether the user can presently log in.
        /// </summary>
        [Display(AutoGenerateField = false)]
        public bool CanLogIn
        {
            get { return !IsLoggingIn; }
        }

        /// <summary>
        ///     Raises operation-related property change notifications
        ///     when the current login operation changes.
        /// </summary>
        private void CurrentLoginOperationChanged()
        {
            RaisePropertyChanged("IsLoggingIn");
            RaisePropertyChanged("CanLogIn");
        }

        /// <summary>
        ///     Creates a new <see cref="LoginParameters" />
        ///     instance using the data stored in this entity.
        /// </summary>
        public LoginParameters ToLoginParameters()
        {
            return new LoginParameters(UserName, Password, RememberMe, null);
        }
    }
}