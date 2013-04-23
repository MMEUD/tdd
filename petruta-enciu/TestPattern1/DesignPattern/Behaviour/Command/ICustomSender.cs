using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public interface ICustomSender 
    {
        void SetCommand(ICustomCommand aCommand, ICustomValidator aValidator);
        // attach a command
        void RemoveCommand();
        // invoke command
        void ExecuteCommand(object args);
    }
}
