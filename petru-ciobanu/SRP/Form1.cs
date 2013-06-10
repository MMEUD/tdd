using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Xml;

namespace SRP
{
    public partial class Form1 : Form
    {
        //private IProductRepository productRepository = new ProductRepository1();
        //private IProductRepository productRepository = new ProductRepository2();
        private IProductRepository productRepository = new ProductRepository3();

        public Form1()
        {
            InitializeComponent();
        }

        private void btnBrowse_Click(object sender, EventArgs e)
        {
            openFileDialog1.Filter = "XML Document (*.xml)|*.xml|All Files (*.*)|*.*";
            var result = openFileDialog1.ShowDialog();
            if (result == DialogResult.OK) {
                txtFileName.Text = openFileDialog1.FileName;
                btnLoad.Enabled = true;
            }
        }

        private void btnLoad_Click(object sender, EventArgs e)
        {
            listView1.Items.Clear();

            var fileName = txtFileName.Text;
            foreach (Product product in productRepository.GetByFileName(fileName))
            {
                var item = new ListViewItem(new[] { 
                    product.Id.ToString(), 
                    product.Name,
                    product.UnitPrice.ToString(), 
                    product.Discontinued.ToString()
                });
                listView1.Items.Add(item);
            }
        }
    }
}
