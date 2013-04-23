using System;
using System.Linq;
using NUnit.Framework;
using NSubstitute;

// references : http://nsubstitute.github.io/
//http://stackoverflow.com/questions/5922009/testing-events-with-nunit-mock-objects

using DesignPatternTests.Behaviour.Command;


namespace DesignPatternTests.Test
{
    [TestFixture]
    public class NunitBehaviorCmdTest
    {
        #region SetUp / TearDown

        [SetUp]
        public void Init()
        { }

        [TearDown]
        public void Dispose()
        { }

        #endregion

        #region CustomValidator

        [Test]
        public void TestCustomValidator_Create()
        {
            // arrange
            IFactory<ICustomValidator> fact = new CustomValidatorFactory();

            //act

            //assert
           Assert.IsNotNull(fact);

           // arrange
            ICustomValidator validator = fact.Create();
           //act

           //assert           
            Assert.IsNotNull(validator);
        }
        #endregion


        #region CustomCommand
        [Test]
        public void TestCustomCommand_CreateFactory()
        {
            // arrange
            var fact = new CustomCommandFactory();
            //act

            //assert            
            Assert.IsNotNull(fact);
        }
        [Test]
        public void TestCustomCommand_Create()
        {
            // arrange
            var fact = Substitute.For<CustomCommandFactory>();
            //act
            CustomCommand cmd = fact.Create() as CustomCommand;
            //assert
            Assert.IsNotNull(cmd);
        }
        [Test]
        public void TestCustomCommand_CreateWithParams()
        {
            // arrange
            var fact = Substitute.For<CustomCommandFactory>();
            var receiver = Substitute.For<ICustomReceiver>();
            var validator = Substitute.For<ICustomValidator>();            
            validator.IsValidArgument(receiver).Returns<bool>(true);    
   
            //act 
            CustomCommand cmd = fact.Create(receiver, validator) as CustomCommand;

            //assert
            Assert.IsNotNull(cmd);
            Assert.IsNotNull(cmd.Receiver);
            Assert.AreSame(receiver, cmd.Receiver);
            Assert.IsNotNull(cmd.Validator);
            Assert.AreSame(validator, cmd.Validator);
 
        }        
        [Test]
        public void TestCustomCommand_AttachTo()
        {
            // arrange
            var fact = Substitute.For<CustomCommandFactory>();
            var receiver = Substitute.For<ICustomReceiver>();
            var validator = Substitute.For<ICustomValidator>();
            validator.IsValidArgument(receiver).Returns<bool>(true);
            
            //act
            CustomCommand cmd = fact.Create(receiver, validator) as CustomCommand;
            cmd.AttachTo(receiver, validator);

            //assert
            Assert.IsNotNull(cmd.Receiver);
            Assert.AreSame(receiver, cmd.Receiver);
            Assert.IsNotNull(cmd.Validator);
            Assert.AreSame(validator, cmd.Validator);
        }
        [Test]
        public void TestCustomCommand_Detach()
        {
            // arrange
            var fact = Substitute.For<CustomCommandFactory>();
            var receiver = Substitute.For<ICustomReceiver>();
            var validator = Substitute.For<ICustomValidator>();
            validator.IsValidArgument(receiver).Returns<bool>(true);

            //act
            CustomCommand cmd = fact.Create(receiver, validator) as CustomCommand;

            //assert
            Assert.IsNotNull(cmd);
            Assert.IsNotNull(cmd.Receiver);
            Assert.AreSame(receiver, cmd.Receiver);
            Assert.IsNotNull(cmd.Validator);
            Assert.AreSame(validator, cmd.Validator);

            //act
            cmd.Dettach();

            //assert
            Assert.IsNotNull(cmd);
            Assert.IsNull(cmd.Receiver);
            Assert.IsNull(cmd.Validator);
        }

        public void TestCustomCommand_CanExecute()
        {
            //arrange
            var fact = Substitute.For<CustomCommandFactory>();

            //act
            CustomCommand cmd = fact.Create() as CustomCommand;

            //assert
            Assert.IsNotNull(cmd);

            //arrange
            object arg1 = null ;
            bool expected1 = false;

            //act
            bool ret1 = cmd.CanExecute(arg1);

            //assert
            Assert.IsFalse(ret1);
            Assert.AreEqual(expected1, ret1);

            //arrange
            object arg2 = new Object();
            bool expected2 = true;

            //act
            bool ret2 = cmd.CanExecute(arg2);

            //assert
            Assert.IsTrue(ret2);
            Assert.AreEqual(expected2, ret2);
        }
        [Test]
        public void TestCustomCommand_Execute()
        {
            //arrange
            var fact = Substitute.For<CustomCommandFactory>();
            var receiver = Substitute.For<ICustomReceiver>();
            var validator = Substitute.For<ICustomValidator>();
            validator.IsValidArgument(receiver).Returns<bool>(true);

            //act
            CustomCommand cmd = fact.Create(receiver, validator) as CustomCommand; 

            //assert
            Assert.IsNotNull(cmd);

            //arrange
            object arg1 = new Object() ;
            validator.IsValidArgument(receiver).Returns<bool>(true);

            //act
            receiver.ClearReceivedCalls();

            //assert
            cmd.Execute(arg1);
            var nn1 = receiver.ReceivedCalls().Count();
            Assert.IsTrue(receiver.ReceivedCalls().Count() == 1);


            //arrange
            object arg2= null;
            validator.IsValidArgument(receiver).Returns<bool>(true);

            //act
            receiver.ClearReceivedCalls();
            cmd.Execute(arg2);
            var nn2 = receiver.ReceivedCalls().Count();

            //assert
            Assert.IsTrue(receiver.ReceivedCalls().Count() == 0);
        }

        #endregion

        [Test]
        public void TestCustomSender()
        {

        }

        [Test]
        public void TestCustomReceiver()
        {
        }

   
    }
}
