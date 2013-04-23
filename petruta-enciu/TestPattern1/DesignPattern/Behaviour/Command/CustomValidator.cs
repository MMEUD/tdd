using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomValidator : ICustomValidator
    {
        public bool IsValidArgument(double args)
        {           
            return ( double.IsNaN(args) ) ? false : true;
        }
        public bool IsValidArgument(string args)
        {
            return ( String.IsNullOrEmpty(args) ) ? false : true;
        }
        public bool IsValidArgument(object args)
        {
            return (args == null) ? false : true;
        }

 
    }
}
