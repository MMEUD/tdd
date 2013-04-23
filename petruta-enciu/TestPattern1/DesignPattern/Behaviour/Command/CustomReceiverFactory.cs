using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomReceiverFactory : IFactory<ICustomReceiver>
    {
        public ICustomReceiver Create()
        {
            return new CustomReceiver(); 
        }

        public ICustomReceiver Create(params dynamic[] args)
        {
            Action<object> anAction = args.OfType<Action<object>>().FirstOrDefault();
            ICustomValidator aValidator = args.OfType<ICustomValidator>().FirstOrDefault();

            if (anAction == null)
            {
                return new CustomReceiver();
            }
            else
            {
                var retObj = new CustomReceiver();
                retObj.AttachCommand(anAction, aValidator);
                return retObj;
            }
 
        }

    }
}
