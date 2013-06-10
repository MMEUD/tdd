using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SRP
{
    class Product
    {
        public int Id { get; set; } 
        public string Name { get; set; } 
        public decimal UnitPrice { get; set; } 
        public bool Discontinued { get; set; }
    }
}
