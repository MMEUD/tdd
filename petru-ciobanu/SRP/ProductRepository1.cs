using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml;

namespace SRP
{
    class ProductRepository1 : IProductRepository
    {
        // La clase es responsable de devolver los productos a partir del nombre de un archivo
        // abrir el archivo p/obtener los datos
        // convertir de xml a objetos
        public IEnumerable<Product> GetByFileName(string fileName)
        {
            var products = new List<Product>(); 
            using (var fs = new FileStream(fileName, FileMode.Open))
            {
                var reader = XmlReader.Create(fs);
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
