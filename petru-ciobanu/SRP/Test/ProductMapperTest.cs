using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace SRP.Test
{
    [TestFixture]
    class ProductMapperTest
    {
        [Test]
        public void Map1Item()
        {
            // arrange
            IProductsMapper productsMapper = new ProductsMapper();
            ASCIIEncoding codificador = new System.Text.ASCIIEncoding();
            Stream stream = new MemoryStream(codificador.GetBytes(
                "<?xml version=\"1.0\" ?><products><product id=\"1\" name=\"IPod Nano\" unitPrice=\"129.55\" discontinued=\"false\"/></products>"));
            // act
            IEnumerable<Product> products = productsMapper.Map(stream);

            // assert
            Assert.That(products, Has.Some.Matches<Product>(p => p.Id == 1));
            Assert.That(products, Has.Some.Matches<Product>(p => p.Name == "IPod Nano"));
            Assert.That(products, Has.Some.Matches<Product>(p => p.UnitPrice == 129.55m));
            Assert.That(products, Has.Some.Matches<Product>(p => p.Discontinued == false));
        }
    }
}
