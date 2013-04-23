using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomSenderFactory : IFactory<ICustomSender>
    {
        public ICustomSender Create()
        {
            return new CustomSender();
        }

        public ICustomSender Create(params dynamic[] args)
        {
            ICustomCommand aCommand = args.OfType<ICustomCommand>().FirstOrDefault();
            ICustomValidator aValidator = args.OfType<ICustomValidator>().FirstOrDefault();

            if (aCommand == null)
            {
                return new CustomSender();
            }
            else
            {
                var retObj = new CustomSender();
                retObj.SetCommand(aCommand, aValidator);
                return retObj;
            }
        }





 
    }
}
