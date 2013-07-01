using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace SRP
{
    interface IFileLoader
    {
        Stream Load(string fileName);
    }
}
