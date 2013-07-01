using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;

namespace SRP
{
    interface IProductsMapper
    {
        IEnumerable<Product> Map(Stream stream);
    }
}
