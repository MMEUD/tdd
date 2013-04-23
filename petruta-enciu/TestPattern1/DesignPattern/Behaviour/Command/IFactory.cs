using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DesignPatternTests.Behaviour.Command
{
    public interface IFactory<T> 
    {
        //create factory
        T Create();

        //create product 
        T Create(params dynamic[] args);

    }
}
