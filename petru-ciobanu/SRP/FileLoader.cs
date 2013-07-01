using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace SRP
{
    class FileLoader : IFileLoader
    {
        public Stream Load(string fileName)
        { 
            return new FileStream(fileName, FileMode.Open); 
        }
    }
}
