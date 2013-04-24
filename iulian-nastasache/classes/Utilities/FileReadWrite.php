<?php
/**
 * Created by JetBrains PhpStorm.
 * User: iulian
 * Date: 4/19/13
 * Time: 5:33 PM
 * Files on disc operations
 */
class FileReadWrite
{

    protected $fileName;
    protected $accessMode;
    protected $fileContent;

    public function __construct($fileName)
    {

        $this->fileName = $fileName;

    }

    public function SetMode($accessMode = "r")
    {
        $this->accessMode = $accessMode;
    }

    public function SetContent($fileContent = "")
    {
        $this->fileContent = $fileContent;
    }

    public function WriteFile()
    {

        $handle = fOpen($this->fileName, $this->accessMode);
        fWrite($handle, $this->fileContent);

        fClose($handle);
    }

    public function ReadFile()
    {

        return file_Get_contents($this->fileName);

    }
}