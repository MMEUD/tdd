using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public interface ICustomCommand
    {
       void AttachTo(ICustomReceiver aReceiver, ICustomValidator aValidator);
       void Dettach();
       //enable/disable command
       bool CanExecute(object args);
       // command body
       void Execute(object args);
    }
}
