using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomSender : ICustomSender
    {
        public ICustomValidator Validator { get; private set;}
        public ICustomCommand Command { get; private set; }

        public void SetCommand(ICustomCommand aCommand, ICustomValidator aValidator = null)
        {
            Validator = (aValidator == null) ? new CustomValidator() : aValidator;

            if (aCommand == null)
            {
                throw new ArgumentNullException("Command is null");
            }
            Command = aCommand;
        }

        public void RemoveCommand()
        {
            Validator = null;
            Command = null;
        }

        public void ExecuteCommand(object args)
        {
            if (Command.CanExecute(args))
            {
                Command.Execute(args);
            }
        }



  
    }
}
