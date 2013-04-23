using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomReceiver :  ICustomReceiver
    {
        public ICustomValidator Validator { get; private set; }
        public Action<object> CommandAction { get; private set; }

        public void AttachCommand(Action<object> anAction, ICustomValidator aValidator = null)
        {
            Validator = (aValidator == null) ? new CustomValidator() : aValidator;

            if (anAction == null)
            {
                throw new ArgumentNullException("Command action is null");
            }

            CommandAction = anAction;
        }

        public void DettachCommand()
        {
            Validator = null;
            CommandAction = null;
        }

        public void Action(object args)
        {
            if (Validator.IsValidArgument(args) && Validator.IsValidArgument(CommandAction))
            {
                var todo = (args is String) ? args : args.ToString();
                CommandAction.Invoke(todo);
            }
        }


  
    }
}
