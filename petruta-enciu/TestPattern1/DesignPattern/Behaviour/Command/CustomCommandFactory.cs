using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomCommandFactory : IFactory<ICustomCommand>
    {
        public ICustomCommand Create()
        {
            return new CustomCommand();
        }

        public ICustomCommand Create(params dynamic[] args)
        {
            ICustomReceiver aReceiver = args.OfType<ICustomReceiver>().FirstOrDefault();
            ICustomValidator aValidator = args.OfType<ICustomValidator>().FirstOrDefault();

            return new CustomCommand(aReceiver, aValidator);
        }
 
    }
}
