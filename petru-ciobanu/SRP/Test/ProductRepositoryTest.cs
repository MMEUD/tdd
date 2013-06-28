using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace SRP.Test
{
    [TestFixture]
    class ProductRepositoryTest
    {
        [Test]
        public void TestRepository11()
        {
            // arrange
            IProductRepository productRepository = new ProductRepository1();

            // act
            IEnumerable<Product> products = productRepository.GetByFileName(AppDomain.CurrentDomain.BaseDirectory + @"\..\..\sample.xml");

            // assert
            Assert.That(products, Has.Some.Matches<Product>(p => p.Name == "IPod Nano"));
        }

        [Test]
        public void TestRepository2()
        {
            // arrange
            IProductRepository productRepository = new ProductRepository2();

            // act
            IEnumerable<Product> products = productRepository.GetByFileName(AppDomain.CurrentDomain.BaseDirectory + @"\..\..\sample.xml");

            // assert
            Assert.That(products, Has.Some.Matches<Product>(p => p.Name == "IPod Nano"));
        }

        [Test]
        public void TestRepository3()
        {
            // arrange
            IProductRepository productRepository = new ProductRepository3();

            // act
            IEnumerable<Product> products = productRepository.GetByFileName(AppDomain.CurrentDomain.BaseDirectory + @"\..\..\sample.xml");

            // assert
            Assert.That(products, Has.Some.Matches<Product>(p => p.Name == "IPod Nano"));
        }
    }
}
