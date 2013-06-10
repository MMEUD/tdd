using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;

namespace SRP
{
    class ProductsMapper : IProductsMapper
    {
        private Product MapItem(XmlReader reader)
        {
            if (reader.Name != "product") throw new InvalidOperationException("XML reader is not on a product fragment.");

            var product = new Product(); 
            product.Id = int.Parse(reader.GetAttribute("id"));
            product.Name = reader.GetAttribute("name"); 
            product.UnitPrice = decimal.Parse(reader.GetAttribute("unitPrice"), CultureInfo.InvariantCulture); 
            product.Discontinued = bool.Parse(reader.GetAttribute("discontinued")); 
            return product; 
        }

        public IEnumerable<Product> Map(Stream stream)
        {
            if (stream == null) throw new ArgumentNullException("Stream used when mapping cannot be null.");
            var reader = XmlReader.Create(stream);

            var products = new List<Product>(); 
            while (reader.Read())
            {
                if (reader.Name != "product") continue;
                var product = MapItem(reader);
                products.Add(product);
            }
            return products;
        }
    }
}
