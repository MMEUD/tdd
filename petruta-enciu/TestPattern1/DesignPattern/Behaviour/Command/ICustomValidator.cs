using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public interface ICustomValidator 
    {
        bool IsValidArgument(object args); 
    }
}
