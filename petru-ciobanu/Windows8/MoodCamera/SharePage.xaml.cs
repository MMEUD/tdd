using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using Windows.UI.Xaml.Media.Imaging;

// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=234238
//  private const string appId = "161840483987433";
namespace MoodCamera
{
    /// <summary>
    /// An empty page that can be used on its own or navigated to within a Frame.
    /// </summary>
    public sealed partial class SharePage : Page
    {
        public SharePage()
        {
            this.InitializeComponent();
            
        }

        /// <summary>
        /// Invoked when this page is about to be displayed in a Frame.
        /// </summary>
        /// <param name="e">Event data that describes how this page was reached.  The Parameter
        /// property is typically used to configure the page.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {


            BitmapImage imagine = e.Parameter as BitmapImage;
            ImageShoots.Source = imagine;

            
            //string name = e.Parameter as string;

            //if (!string.IsNullOrWhiteSpace(name))
            //{
            //    tb1.Text = "Hello, " + name;
            //}
            //else
            //{
            //    tb1.Text = "Name is required.  Go back and enter a name.";
            //}



        }


        public  BitmapImage createImage(BitmapImage image){
            return image;
        }
    }
}
