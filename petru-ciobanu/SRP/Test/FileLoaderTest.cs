using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;

namespace SRP.Test
{
    [TestFixture]
    class FileLoaderTest
    {
        [Test]
        public void OpenFile()
        {
            // arrange
            IFileLoader fileLoader = new FileLoader();

            // act
            using (Stream stream = fileLoader.Load(AppDomain.CurrentDomain.BaseDirectory + @"\..\..\sample.xml"))
            {
                // assert
                Assert.That(stream, Is.Not.Null);
            }
        }
    }
}
