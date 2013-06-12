using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml;

namespace SRP
{
    class ProductRepository2 : IProductRepository
    {
        private readonly IFileLoader loader; 

        public ProductRepository2()
        {
            loader = new FileLoader(); 
        }

        // La clase es responsable de devolver los productos a partir del nombre de un archivo
        // convertir de xml a objetos
        public IEnumerable<Product> GetByFileName(string fileName)
        {
            var products = new List<Product>(); 
            using (Stream input = loader.Load(fileName))
            {
                var reader = XmlReader.Create(input);
                while (reader.Read())
                {
                    if (reader.Name != "product") continue;
                    var product = new Product();
                    product.Id = int.Parse(reader.GetAttribute("id"));
                    product.Name = reader.GetAttribute("name");
                    product.UnitPrice = decimal.Parse(reader.GetAttribute("unitPrice"));
                    product.Discontinued = bool.Parse(reader.GetAttribute("discontinued"));
                    products.Add(product);
                }
            }
            return products;
        }
    }
}
