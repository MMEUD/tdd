using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public interface ICustomReceiver 
    {
        void AttachCommand(Action<object> anAction, ICustomValidator aValidator);
        void DettachCommand();
        // command action
        void Action(object args);
    }
}
