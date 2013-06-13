using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Windows.Devices.Enumeration;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Media.Capture;
using Windows.Media.MediaProperties;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;
using Windows.ApplicationModel.DataTransfer;
// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234238

namespace MoodCamera
{
    public sealed partial class CameraPage : Page
    {


        private DispatcherTimer timer = new DispatcherTimer();
        private DeviceInformationCollection m_devInfoCollection;
        private Windows.Media.Capture.MediaCapture m_mediaCaptureMgr;
        private DataTransferManager dataTransferManager;
        private readonly String PHOTO_FILE_NAME = "photo.jpg";
        private int timeLeft = 5;
        public CameraPage()
        {
            this.InitializeComponent();
            this.Tapped += CameraPage_Tapped;
            CounterText.Text = timeLeft.ToString();
            CounterText.Visibility = Visibility.Collapsed;
            PhotoCapture.Visibility = Visibility.Collapsed;
            BtnShare.Visibility = Visibility.Collapsed;
            EnumerateWebcamsAsync();
        }

        private void CameraPage_Tapped(object sender, TappedRoutedEventArgs e)
        {
            CounterText.Visibility = Visibility.Visible;
            timer.Interval = new TimeSpan(0, 0, 1);
            timer.Tick += timer_Tick;
            timer.Start();
        }
        private void timer_Tick(object sender, object e)
        {
            if (timeLeft > 0)
            {
                timeLeft -= 1;
                CounterText.Text = Convert.ToString(timeLeft);
            }
            else
            {
                timer.Stop();
                CounterText.Visibility = Visibility.Collapsed;
                TakePhoto();
            }
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
          
        }

        private async void EnumerateWebcamsAsync()
        {
            try
            {
                m_devInfoCollection = null;
                CameraListDevice.Items.Clear();
                m_devInfoCollection = await DeviceInformation.FindAllAsync(DeviceClass.VideoCapture);
                if (m_devInfoCollection.Count == 0)
                {
                    await new Windows.UI.Popups.MessageDialog("No WebCams found.").ShowAsync();
                }
                else
                {
                    for (int i = 0; i < m_devInfoCollection.Count; i++)
                    {
                        var devInfo = m_devInfoCollection[i];
                        CameraListDevice.Items.Add(devInfo.Name);
                    }
                    CameraListDevice.SelectedIndex = 1;
                    StartPreview();
                }
            }
            catch (Exception e)
            {

            }
        }

        private async void StartPreview()
        {
            try
            {
                m_mediaCaptureMgr = new Windows.Media.Capture.MediaCapture();
                var settings = new Windows.Media.Capture.MediaCaptureInitializationSettings();
                var chosenDevInfo = m_devInfoCollection[CameraListDevice.SelectedIndex];
                settings.VideoDeviceId = chosenDevInfo.Id;
                await m_mediaCaptureMgr.InitializeAsync(settings);
                CameraPreview.Source = m_mediaCaptureMgr;
                await m_mediaCaptureMgr.StartPreviewAsync();
            }
            catch (Exception e)
            {

                 new Windows.UI.Popups.MessageDialog("StartPreviewErrors").ShowAsync();
            }

        }


        private async void TakePhoto()
        {
            try
            {
                ImageEncodingProperties imageProperties = ImageEncodingProperties.CreateJpeg();
                var fPhotoStream = new InMemoryRandomAccessStream();
                await m_mediaCaptureMgr.CapturePhotoToStreamAsync(imageProperties, fPhotoStream);
                await fPhotoStream.FlushAsync();
                fPhotoStream.Seek(0);

                BitmapImage image = new BitmapImage();
                image.SetSource(fPhotoStream);
                image.DecodePixelWidth = 420;
                image.DecodePixelHeight = 320;
                PhotoCapture.Source = image;
                PhotoCapture.Stretch = Stretch.UniformToFill;
               // this.Frame.Navigate(typeof(SharePage), image);

                m_mediaCaptureMgr.StopPreviewAsync();
                PhotoCapture.Visibility = Visibility.Visible;
                this.Tapped -= CameraPage_Tapped;
                BtnShare.Visibility = Visibility.Visible;
            }
            catch (Exception)
            {
                
                new Windows.UI.Popups.MessageDialog("TakePhotoExceptions").ShowAsync();
            }
        }


        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
          
        }

 

        private void BtnShare_Click_1(object sender, RoutedEventArgs e)
        {
            // If the user clicks the share button, 
            //invoke the share flow programatically.
           
        }

    }
}
