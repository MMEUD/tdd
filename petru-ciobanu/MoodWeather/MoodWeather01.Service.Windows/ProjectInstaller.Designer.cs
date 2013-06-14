namespace MoodWeather.Service.Windows
{
    partial class ProjectInstaller
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.WeatherProcessInstaller = new System.ServiceProcess.ServiceProcessInstaller();
            this.WeatherInstaller = new System.ServiceProcess.ServiceInstaller();
            // 
            // WeatherProcessInstaller
            // 
            this.WeatherProcessInstaller.Account = System.ServiceProcess.ServiceAccount.LocalSystem;
            this.WeatherProcessInstaller.Password = null;
            this.WeatherProcessInstaller.Username = null;
            // 
            // WeatherInstaller
            // 
            this.WeatherInstaller.Description = "Get Weather Forecast";
            this.WeatherInstaller.ServiceName = "ServiceMoodWeather";
            // 
            // ProjectInstaller
            // 
            this.Installers.AddRange(new System.Configuration.Install.Installer[] {
            this.WeatherProcessInstaller,
            this.WeatherInstaller});

        }

        #endregion

        private System.ServiceProcess.ServiceInstaller WeatherInstaller;
        private System.ServiceProcess.ServiceProcessInstaller WeatherProcessInstaller;
    }
}