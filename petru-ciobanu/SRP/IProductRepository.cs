using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SRP
{
    interface IProductRepository
    {
        IEnumerable<Product> GetByFileName(string fileName);
    }
}
