using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml;

namespace SRP
{
    class ProductRepository3 : IProductRepository
    {
        private readonly IFileLoader loader; 
        private readonly IProductsMapper mapper;

        public ProductRepository3()
        {
            loader = new FileLoader(); 
            mapper = new ProductsMapper();
        }

        // La clase es responsable de devolver los productos a partir del nombre de un archivo
        // convertir de xml a objetos
        public IEnumerable<Product> GetByFileName(string fileName)
        {
            IEnumerable<Product> products; 
            using (Stream file = loader.Load(fileName)) {
                products = mapper.Map(file);
            }
            return products;
        }
    }
}
