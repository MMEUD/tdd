using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomCommand : ICustomCommand
    {
        public ICustomValidator Validator { get; private set; }
        public ICustomReceiver Receiver { get; private set; }

        public CustomCommand()
        {
        }

        public CustomCommand(ICustomReceiver aReceiver, ICustomValidator aValidator = null)
        {
            Validator = (aValidator == null) ? new CustomValidator() : aValidator;

            if (!Validator.IsValidArgument(aReceiver))
            {
                throw new ArgumentNullException("Command receiver cannot be null");
            }
            Receiver = aReceiver;
        }

        public void AttachTo(ICustomReceiver aReceiver, ICustomValidator aValidator = null)
        {
            Validator = (aValidator == null) ? new CustomValidator() : aValidator;

            if (!Validator.IsValidArgument(aReceiver))
            {
                throw new ArgumentNullException("Command receiver cannot be null");
            }
            Receiver = aReceiver;
        }

        public void Dettach()
        {
            Receiver = null;
            Validator = null;
        }

        public bool CanExecute(object args)
        {
            return (args != null) ? true : false;
        }

        public void Execute(object args)
        {
            if (CanExecute(args) && Validator.IsValidArgument(Receiver))
            {
                Receiver.Action(args);
            }
        }

    }
}
