﻿#pragma checksum "H:\MoodWeatherFinal\MoodWeather01\Views\Controls\ApiKeyManagement.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "72E44D88F22AA3A639B4DB162556B0F4"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.17929
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using System;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Automation.Peers;
using System.Windows.Automation.Provider;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Interop;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Imaging;
using System.Windows.Resources;
using System.Windows.Shapes;
using System.Windows.Threading;


namespace MoodWeather.Views.Controls {
    
    
    public partial class ApiKeyManagement : System.Windows.Controls.UserControl {
        
        internal System.Windows.Controls.Grid LayoutRoot;
        
        internal System.Windows.Controls.DomainDataSource weatherSetupDomainDataSource;
        
        internal System.Windows.Controls.Grid grid1;
        
        internal System.Windows.Controls.TextBox api_callsTextBox;
        
        internal System.Windows.Controls.TextBox api_keyTextBox;
        
        internal System.Windows.Controls.TextBox api_urlTextBox;
        
        internal System.Windows.Controls.TextBox apic_call_minuteTextBox;
        
        internal System.Windows.Controls.TextBox idTextBox;
        
        internal System.Windows.Controls.TextBox activeTextBox;
        
        internal Telerik.Windows.Controls.RadGridView RadGridListApi;
        
        internal System.Windows.Controls.Button BtnSave;
        
        internal System.Windows.Controls.Button BtnDelete;
        
        internal System.Windows.Controls.Button BtnInsert;
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Windows.Application.LoadComponent(this, new System.Uri("/MoodWeather;component/Views/Controls/ApiKeyManagement.xaml", System.UriKind.Relative));
            this.LayoutRoot = ((System.Windows.Controls.Grid)(this.FindName("LayoutRoot")));
            this.weatherSetupDomainDataSource = ((System.Windows.Controls.DomainDataSource)(this.FindName("weatherSetupDomainDataSource")));
            this.grid1 = ((System.Windows.Controls.Grid)(this.FindName("grid1")));
            this.api_callsTextBox = ((System.Windows.Controls.TextBox)(this.FindName("api_callsTextBox")));
            this.api_keyTextBox = ((System.Windows.Controls.TextBox)(this.FindName("api_keyTextBox")));
            this.api_urlTextBox = ((System.Windows.Controls.TextBox)(this.FindName("api_urlTextBox")));
            this.apic_call_minuteTextBox = ((System.Windows.Controls.TextBox)(this.FindName("apic_call_minuteTextBox")));
            this.idTextBox = ((System.Windows.Controls.TextBox)(this.FindName("idTextBox")));
            this.activeTextBox = ((System.Windows.Controls.TextBox)(this.FindName("activeTextBox")));
            this.RadGridListApi = ((Telerik.Windows.Controls.RadGridView)(this.FindName("RadGridListApi")));
            this.BtnSave = ((System.Windows.Controls.Button)(this.FindName("BtnSave")));
            this.BtnDelete = ((System.Windows.Controls.Button)(this.FindName("BtnDelete")));
            this.BtnInsert = ((System.Windows.Controls.Button)(this.FindName("BtnInsert")));
        }
    }
}

