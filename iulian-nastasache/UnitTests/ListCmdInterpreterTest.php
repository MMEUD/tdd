<?php
/**
 * Generated by PHPUnit_SkeletonGenerator 1.2.0 on 2013-04-25 at 10:45:12.
 */
class ListCmdInterpreterTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var ListCmdInterpreter
     */
    protected $object;

    /**
     * Sets up the fixture, for example, opens a network connection.
     * This method is called before a test is executed.
     */
    protected function setUp()
    {
        $new_group = new IniGroup();
        $new_group->SetProperty("a", 7);
        IniGroupsContainer::AddIniGroup("testIniGroup", $new_group);
        IniGroupsContainer::SetCurentIniGroup("testIniGroup");

        $this->object = new ListCmdInterpreter;
    }

    /**
     * Tears down the fixture, for example, closes a network connection.
     * This method is called after a test is executed.
     */
    protected function tearDown()
    {
    }

    /**
     * @covers ListCmdInterpreter::CommandInterpreter
     * @todo   Implement testCommandInterpreter().
     */
    public function testCommandInterpreter()
    {
        //$this->object->SetCommandParams("general");
        $this->assertSame("testIniGroup : a = 7\n",$this->object->CommandInterpreter());
        $this->assertNotEquals("testIniGroup : a = \n",$this->object->CommandInterpreter());
    }
}
