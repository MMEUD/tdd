using System;
using System.Windows;
using System.Windows.Controls;

using MoodWeather.Web;

namespace MoodWeather.Views.Login
{
    public partial class ForgotPassword : ChildWindow
    {
#pragma warning disable 649
        private ForgotInfo registrationData;
#pragma warning restore 649
        //  private PasswordContext context= new PasswordContext();
        public ForgotPassword()
        {
            InitializeComponent();
            DataContext = registrationData;
        }

        private void btnSend_Click(object sender, RoutedEventArgs e)
        {
            //status.Content = "";
            //registrationData = new ForgotInfo();
            //if (this.forgotForm.ValidateItem())
            //{
            //    this.busyIndicator.IsBusy = true;
            //    this.busyIndicator.BusyContent = "SendDataToServer";
            //    context.Load(context.RetriveUserPasswordQueryQuery(registrationData.Email), null, false).Completed += (lo, args) =>
            //  {
            //      this.busyIndicator.BusyContent = "SendDataToServer";
            //      this.busyIndicator.IsBusy = false;

            //      if (context.aspnet_FindUserByMails.Count == 0)
            //      {

            //          status.Content = "Cant find registered account with this email";
            //          this.busyIndicator.IsBusy = false;
            //      }
            //      else
            //      {
            //          status.Content = context.aspnet_FindUserByMails.ElementAt(0).Password;
            //          EmailNewPassword(registrationData.Email, context.aspnet_FindUserByMails.ElementAt(0).UserName, context.aspnet_FindUserByMails.ElementAt(0).Password, "MoodWeather");
            //      }
            //  };


            //}
        }

        private void btnCancel_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                if (registrationData.CurrentOperation != null && registrationData.CurrentOperation.CanCancel)
                {
                    registrationData.CurrentOperation.Cancel();
                }
                else
                {
                    DialogResult = false;
                }
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
            }
        }


        //private void getPassword()
        //{
        //    try
        //    {
        //        DomainContext domainContext = new PasswordContext();
        //        var domainDataSource = new DomainDataSource
        //            {
        //                DomainContext = domainContext,
        //                QueryName = "RetriveUserPasswordQuery"
        //            };
        //        var param = new Parameter { ParameterName = "emailz", Value = registrationData.Email };
        //        domainDataSource.QueryParameters.Add(param);
        //        domainDataSource.Load();
        //        MessageBox.Show(domainDataSource.FindName("Password").ToString());
        //    }
        //    catch (Exception e)
        //    {
        //        MessageBox.Show(e.Message.ToString());
        //    }

        //}

        // Email Password To User Account Email
        private void EmailNewPassword(string email, string userName, string password, string loginUrl)
        {
            //string message = string.Format("Your user name is {0}\r\n", userName);
            //message += string.Format("Your password is {0}\r\n", password);
            //message += "\r\n";
            //message += string.Format("To login go to {0}\r\n", loginUrl);

            //string mailTo = email;
            //string subject = "Your  password for MoodWeather";

            //// TODO: You need to replace this with your own e-mail code,
            //// something along the lines of MyUtilities.EMail.Send(mailTo, subject, message)
            //// for now we just fake the e-mail action by logging the message to a text file. 
            //string tempFile = @"D:\\tmp\\emailog.txt";
            //string tempLogMessage = string.Format("to: {0}\r\nsubject: {1}\r\nmessage: {2}\r\n\r\n", mailTo, subject, message);
            ////System.IO.File.AppendAllText(tempFile, tempLogMessage);
        }

        private void forgotForm_AutoGeneratingField(object sender, DataFormAutoGeneratingFieldEventArgs e)
        {
            // Put all the fields in adding mode
            e.Field.Mode = DataFieldMode.AddNew;
            if (e.PropertyName == "Email")
            {
                var textBox = (TextBox) e.Field.Content;
                textBox.Width = 300;
                textBox.HorizontalAlignment = HorizontalAlignment.Left;
            }
        }

        private void aspnet_FindUserByMailDomainDataSource_LoadedData(object sender, LoadedDataEventArgs e)
        {
            if (e.HasError)
            {
                MessageBox.Show(e.Error.ToString(), "Load Error", MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }

        private void aspnet_FindUserByMailDomainDataSource_LoadedData_1(object sender, LoadedDataEventArgs e)
        {
            if (e.HasError)
            {
                MessageBox.Show(e.Error.ToString(), "Load Error", MessageBoxButton.OK);
                e.MarkErrorAsHandled();
            }
        }
    }
}