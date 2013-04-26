<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Cristina Condruz
 * Date: 4/19/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
require_once('CommandExpression.php');
require_once('NamespaceProperty.php');
require_once('MySingleton.php');

class LoadExpression implements CommandExpression{
   private $argument;
   private $msg;
   public function __construct($argument){
       $this->argument = $argument;
   }
   public function setArgument($argument){
         $this->argument = $argument;
   }
   public function getArgument(){
           return $this->argument;
   }
  public function interpret(){
      //the command should be load or load {namespace_name}
      if ($this->getArgument() != ""){
        //load {namespace_name}.properties
        $this->loadNamespace($this->getArgument());
      }else{
        //loads all the files .properties
        print_r(glob("properties/*.properties"));
        foreach(glob("properties/*.properties") as $filename){
          $filenameWithoutDirPath = str_replace('properties/', '', $filename);
          $filenameWithoutExt = str_replace('.properties', '', $filenameWithoutDirPath);
          $this->loadNamespace($filenameWithoutExt);
        }
      }
      return $this->msg;
    }

   public function loadNamespace($name){
      $currentSession = MySingleton::getInstance();
      $allNamespaces = $currentSession->getNamespaces();
      if(array_key_exists($name,$allNamespaces)){
        if (is_file("properties/".$name.'.properties')) {
           $lines = file("properties/".$name.'.properties', FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
           //var_dump($lines);
           $nrParam = 0;
           foreach ($lines as $line) {
             $param_value = explode('=', $line);
             if (count($param_value) == 2) {
               $currentProperty = new NamespaceProperty($param_value[0],$param_value[1]);

               $selectedNamespace = $currentSession->getNamespace($name);
               $selectedNamespace->addProperties($currentProperty);
               $nrParam++;
             }
           }
          $this->msg .= $selectedNamespace->getName().": loaded ".$nrParam." parameters\n";
         } else {
          $this->msg .= "The file $name.properties not found\n";
         }
      }else{
        $this->msg .= "There is no namespace named: ".$name;
      }
    }
}
