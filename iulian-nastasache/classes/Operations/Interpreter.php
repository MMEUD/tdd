<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:38 PM
 * Split console line into command and parameters; translate to load specific command.
 ****** Pattern: Interpreter
 */
class Interpreter
{

    private $commandString;

    public function __construct($commandString)
    {
        $this->commandString = $commandString;

    }

    public function Interpret()
    {
        $commandComponents = exPlode(" ", $this->commandString);

        $command = ucFirst($commandComponents[0]);

        array_Shift($commandComponents);

        if ($command == "") {
            return $this->CommandMessage("Command missing\n");
        } elseif($command == QUIT_COMMAND) {
            return $this->CommandMessage("Goodbye\n");
        }

        $className = $command . "CmdInterpreter";

        // Possible TODO: return operation here, instantiate operation object not in interpreter

        if (is_File(COMMANDS_FOLDER.$className.'.php') && class_Exists($className)) {

            $operation = new $className();
            $operation->SetCommandParams($commandComponents);
            $output = $operation->CommandInterpreter();

            return $this->CommandMessage($output);

        } else {
            return $this->CommandMessage("Command '$command' not implemented");
        }

    }

    private function CommandMessage($message)
    {
        echo $message . "\n";

    }
}