<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
class IniGroup
{
    private $properties;

    function __construct()
    {
        $this->properties = new ArrayObject();
    }

    public function SetProperty($key,$val) {

        $iterator = $this->ReadProperties();

        $found = false;

        while($iterator->valid()) {

            if($iterator->current()->GetKey() == $key) {
                $iterator->current()->SetValue($val);
                $found = true;
            }

            $iterator->next();
        }

        if ($found == false){
            $this->properties->append(new Property($key, $val));
        }

    }


    public function ReadProperties() {
        return $this->properties->getIterator();
    }


    public function SetProperties($string)
    {

        $stringLines = exPlode("\n", $string);
        foreach ($stringLines as $value) {
            $valArr = exPlode(" = ", $value);
            if (coUnt($valArr) == 2) {
                $this->SetProperty($valArr[0], $valArr[1]);
            }
        }

    }

    public function GetPropertiesCount() {
        return $this->properties->count();

    }
}