using System;
using System.ComponentModel.DataAnnotations;
using System.ServiceModel.DomainServices.Client.ApplicationServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using MoodWeather.Web;
using MoodWeather.Views.Login;

namespace MoodWeather.LoginUI
{
    /// <summary>
    ///     Form that presents the login fields and handles the login process.
    /// </summary>
    public partial class LoginForm : StackPanel
    {
        private readonly LoginInfo loginInfo = new LoginInfo();
        private LoginRegistrationWindow parentWindow;

        /// <summary>
        ///     Creates a new <see cref="LoginForm" /> instance.
        /// </summary>
        public LoginForm()
        {
            InitializeComponent();

            // Set the DataContext of this control to the
            // LoginInfo instance to allow for easy binding
            DataContext = loginInfo;
        }

        /// <summary>
        ///     Sets the parent window for the current <see cref="LoginForm" />.
        /// </summary>
        /// <param name="window">The window to use as the parent.</param>
        public void SetParentWindow(LoginRegistrationWindow window)
        {
            parentWindow = window;
        }

        /// <summary>
        ///     Handles <see cref="DataForm.AutoGeneratingField" /> to provide the PasswordAccessor.
        /// </summary>
        private void LoginForm_AutoGeneratingField(object sender, DataFormAutoGeneratingFieldEventArgs e)
        {
            if (e.PropertyName == "Password")
            {
                var passwordBox = (PasswordBox) e.Field.Content;
                loginInfo.PasswordAccessor = () => passwordBox.Password;
            }
        }

        /// <summary>
        ///     Submits the <see cref="LoginOperation" /> to the server
        /// </summary>
        private void LoginButton_Click(object sender, EventArgs e)
        {
            // We need to force validation since we are not using the standard OK
            // button from the DataForm.  Without ensuring the form is valid, we
            // would get an exception invoking the operation if the entity is invalid.
            if (loginForm.ValidateItem())
            {
                loginInfo.CurrentLoginOperation = WebContext.Current.Authentication.Login(
                    loginInfo.ToLoginParameters(), LoginOperation_Completed, null);
                parentWindow.AddPendingOperation(loginInfo.CurrentLoginOperation);
            }
        }

        /// <summary>
        ///     Completion handler for a <see cref="LoginOperation" />. If operation succeeds,
        ///     it closes the window. If it has an error, we show an <see cref="ErrorWindow" />
        ///     and mark the error as handled. If it was not canceled, but login failed, it must
        ///     have been because credentials were incorrect so we add a validation error to
        ///     to notify the user.
        /// </summary>
        private void LoginOperation_Completed(LoginOperation loginOperation)
        {
            if (loginOperation.LoginSuccess)
            {
                parentWindow.Close();
            }
            else if (loginOperation.HasError)
            {
                ErrorWindow.CreateNew(loginOperation.Error);
                loginOperation.MarkErrorAsHandled();
            }
            else if (!loginOperation.IsCanceled)
            {
                loginInfo.ValidationErrors.Add(new ValidationResult(ErrorResources.ErrorBadUserNameOrPassword,
                                                                    new[] {"UserName", "Password"}));
            }
        }

        /// <summary>
        ///     Switches to the registration form.
        /// </summary>
        private void RegisterNow_Click(object sender, RoutedEventArgs e)
        {
            parentWindow.NavigateToRegistration();
        }

        /// <summary>
        ///     If a login operation is in progress and is cancellable, cancel it.
        ///     Otherwise, close the window.
        /// </summary>
        private void CancelButton_Click(object sender, EventArgs e)
        {
            if (loginInfo.CurrentLoginOperation != null && loginInfo.CurrentLoginOperation.CanCancel)
            {
                loginInfo.CurrentLoginOperation.Cancel();
            }
            else
            {
                parentWindow.Close();
            }
        }

        /// <summary>
        ///     Maps Esc to the cancel button and Enter to the OK button.
        /// </summary>
        private void LoginForm_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Escape)
            {
                CancelButton_Click(sender, e);
            }
            else if (e.Key == Key.Enter && loginButton.IsEnabled)
            {
                LoginButton_Click(sender, e);
            }
        }

        private void btnForgot_Click(object sender, RoutedEventArgs e)
        {
            parentWindow.Close();

            var form = new ForgotPassword();
            form.Show();
        }
    }
}