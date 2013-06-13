using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Globalization;
using System.Linq;
using System.ServiceModel.DomainServices.Client;
using System.ServiceModel.DomainServices.Client.ApplicationServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;

using MoodWeather.Web;
using MoodWeather.Web.Services;


namespace MoodWeather.Views.Login
{
    /// <summary>
    ///     Form that presents the <see cref="RegistrationData" /> and performs the registration process.
    /// </summary>
    public partial class RegistrationForm : StackPanel
    {
        private readonly RegistrationData registrationData = new RegistrationData();
        private readonly UserRegistrationContext userRegistrationContext = new UserRegistrationContext();
        private LoginRegistrationWindow parentWindow;

        /// <summary>
        ///     Creates a new <see cref="RegistrationForm" /> instance.
        /// </summary>
        public RegistrationForm()
        {
            InitializeComponent();

            // Set the DataContext of this control to the
            // Registration instance to allow for easy binding.
            DataContext = registrationData;
        }

        /// <summary>
        ///     Sets the parent window for the current <see cref="RegistrationForm" />.
        /// </summary>
        /// <param name="window">The window to use as the parent.</param>
        public void SetParentWindow(LoginRegistrationWindow window)
        {
            parentWindow = window;
        }

        /// <summary>
        ///     Wire up the Password and PasswordConfirmation Accessors as the fields get generated.
        /// </summary>
        private void RegisterForm_AutoGeneratingField(object dataForm, DataFormAutoGeneratingFieldEventArgs e)
        {
            // Put all the fields in adding mode
            e.Field.Mode = DataFieldMode.AddNew;

            if (e.PropertyName == "Password")
            {
                var passwordBox = (PasswordBox) e.Field.Content;
                registrationData.PasswordAccessor = () => passwordBox.Password;
            }
            else if (e.PropertyName == "PasswordConfirmation")
            {
                var passwordConfirmationBox = (PasswordBox) e.Field.Content;
                registrationData.PasswordConfirmationAccessor = () => passwordConfirmationBox.Password;
            }
            else if (e.PropertyName == "UserName")
            {
                var textBox = (TextBox) e.Field.Content;
                textBox.LostFocus += UserNameLostFocus;
            }
            else if (e.PropertyName == "Question")
            {
                // Create a ComboBox populated with security questions
                ComboBox comboBoxWithSecurityQuestions = CreateComboBoxWithSecurityQuestions();

                // Replace the control
                // Note: Since TextBox.Text treats empty text as string.Empty and ComboBox.SelectedItem
                // treats an empty selection as null, we need to use a converter on the binding
                e.Field.ReplaceTextBox(comboBoxWithSecurityQuestions, Selector.SelectedItemProperty,
                                       binding => binding.Converter = new TargetNullValueConverter());
            }
            else if (e.PropertyName == "Role")
            {
                // Create a ComboBox populated with security questions
                ComboBox comboBoxWithUserRole = CreateComboBoxWithRoleManager();

                // Replace the control
                // Note: Since TextBox.Text treats empty text as string.Empty and ComboBox.SelectedItem
                // treats an empty selection as null, we need to use a converter on the binding
                e.Field.ReplaceTextBox(comboBoxWithUserRole, Selector.SelectedItemProperty,
                                       binding => binding.Converter = new TargetNullValueConverter());
            }
        }


        /// <summary>
        ///     Returns a <see cref="ComboBox" /> object whose <see cref="ComboBox.ItemsSource" /> property
        ///     is initialized with the resource strings defined in <see cref="UserRoleManager" />.
        /// </summary>
        private static ComboBox CreateComboBoxWithRoleManager()
        {
            // Use reflection to grab all the localized security questions
            IEnumerable<string> availableRole = from propertyInfo in typeof (UserRoleManager).GetProperties()
                                                where propertyInfo.PropertyType.Equals(typeof (string))
                                                select (string) propertyInfo.GetValue(null, null);

            // Create and initialize the ComboBox object
            var comboBox = new ComboBox();
            comboBox.ItemsSource = availableRole;
            return comboBox;
        }

        /// <summary>
        ///     The callback for when the UserName TextBox loses focus.  Call into the
        ///     registration data to allow logic to be processed, possibly setting
        ///     the FriendlyName field.
        /// </summary>
        /// <param name="sender">The event sender.</param>
        /// <param name="e">The event args.</param>
        private void UserNameLostFocus(object sender, RoutedEventArgs e)
        {
            registrationData.UserNameEntered(((TextBox) sender).Text);
        }

        /// <summary>
        ///     Returns a <see cref="ComboBox" /> object whose <see cref="ComboBox.ItemsSource" /> property
        ///     is initialized with the resource strings defined in <see cref="SecurityQuestions" />.
        /// </summary>
        private static ComboBox CreateComboBoxWithSecurityQuestions()
        {
            // Use reflection to grab all the localized security questions
            IEnumerable<string> availableQuestions = from propertyInfo in typeof (SecurityQuestions).GetProperties()
                                                     where propertyInfo.PropertyType.Equals(typeof (string))
                                                     select (string) propertyInfo.GetValue(null, null);

            // Create and initialize the ComboBox object
            var comboBox = new ComboBox();
            comboBox.ItemsSource = availableQuestions;
            return comboBox;
        }

        /// <summary>
        ///     Submit the new registration.
        /// </summary>
        private void RegisterButton_Click(object sender, RoutedEventArgs e)
        {
            // We need to force validation since we are not using the standard OK
            // button from the DataForm.  Without ensuring the form is valid, we
            // would get an exception invoking the operation if the entity is invalid.
            if (registerForm.ValidateItem())
            {
                registrationData.CurrentOperation = userRegistrationContext.CreateUser(
                    registrationData,
                    registrationData.Password,
                    RegistrationOperation_Completed, null);

                parentWindow.AddPendingOperation(registrationData.CurrentOperation);
            }
        }

        /// <summary>
        ///     Completion handler for the registration operation. If there was an error, an
        ///     <see cref="ErrorWindow" /> is displayed to the user. Otherwise, this triggers
        ///     a login operation that will automatically log in the just registered user.
        /// </summary>
        private void RegistrationOperation_Completed(InvokeOperation<CreateUserStatus> operation)
        {
            if (!operation.IsCanceled)
            {
                if (operation.HasError)
                {
                    ErrorWindow.CreateNew(operation.Error);
                    operation.MarkErrorAsHandled();
                }
                else if (operation.Value == CreateUserStatus.Success)
                {
                    parentWindow.Close();
                    // this.registrationData.CurrentOperation = WebContext.Current.Authentication.Login(this.registrationData.ToLoginParameters(), this.LoginOperation_Completed, null);
                    //this.parentWindow.AddPendingOperation(this.registrationData.CurrentOperation);
                }
                else if (operation.Value == CreateUserStatus.DuplicateUserName)
                {
                    registrationData.ValidationErrors.Add(
                        new ValidationResult(ErrorResources.CreateUserStatusDuplicateUserName, new[] {"UserName"}));
                }
                else if (operation.Value == CreateUserStatus.DuplicateEmail)
                {
                    registrationData.ValidationErrors.Add(
                        new ValidationResult(ErrorResources.CreateUserStatusDuplicateEmail, new[] {"Email"}));
                }
                else
                {
                    ErrorWindow.CreateNew(ErrorResources.ErrorWindowGenericError);
                }
            }
        }

        /// <summary>
        ///     Completion handler for the login operation that occurs after a successful
        ///     registration and login attempt.  This will close the window. On the unexpected
        ///     event that this operation failed (the user was just added so why wouldn't it
        ///     be authenticated successfully) an  <see cref="ErrorWindow" /> is created and
        ///     will display the error message.
        /// </summary>
        /// <param name="loginOperation">
        ///     The <see cref="LoginOperation" /> that has completed.
        /// </param>
        private void LoginOperation_Completed(LoginOperation loginOperation)
        {
            if (!loginOperation.IsCanceled)
            {
                parentWindow.Close();

                if (loginOperation.HasError)
                {
                    ErrorWindow.CreateNew(string.Format(CultureInfo.CurrentUICulture,
                                                        ErrorResources.ErrorLoginAfterRegistrationFailed,
                                                        loginOperation.Error.Message));
                    loginOperation.MarkErrorAsHandled();
                }
                else if (loginOperation.LoginSuccess == false)
                {
                    // The operation was successful, but the actual login was not
                    ErrorWindow.CreateNew(string.Format(CultureInfo.CurrentUICulture,
                                                        ErrorResources.ErrorLoginAfterRegistrationFailed,
                                                        ErrorResources.ErrorBadUserNameOrPassword));
                }
            }
        }

        /// <summary>
        ///     Switches to the login window.
        /// </summary>
        private void BackToLogin_Click(object sender, RoutedEventArgs e)
        {
            parentWindow.NavigateToLogin();
        }

        /// <summary>
        ///     If a registration or login operation is in progress and is cancellable, cancel it.
        ///     Otherwise, close the window.
        /// </summary>
        private void CancelButton_Click(object sender, EventArgs e)
        {
            if (registrationData.CurrentOperation != null && registrationData.CurrentOperation.CanCancel)
            {
                registrationData.CurrentOperation.Cancel();
            }
            else
            {
                parentWindow.Close();
            }
        }
    }
}