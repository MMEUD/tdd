using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public class CustomValidatorFactory : IFactory<ICustomValidator>
    {
        public ICustomValidator Create()
        {
            return new CustomValidator();
        }

        public ICustomValidator Create(params dynamic[] args)
        {
            return new CustomValidator();
        }
    }
}
