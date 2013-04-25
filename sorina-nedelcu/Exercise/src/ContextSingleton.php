<?

  class ContextSingleton  {

    // singleton instance
    private static $instance = null;
    private $namespace = array(); // array of NamespaceObject
    private $currentNamespace;

    // private constructor function
    // to prevent external instantiation
    private function __construct() { }

    // getInstance method
    public static function getInstance() {

      if(!self::$instance) {
        self::$instance = new self();
      }
      return self::$instance;
    }

    public function addNamespace($currentNamespace,$namespaceObj) {
      $this->namespace[$currentNamespace] = $namespaceObj;
    }

    public function getNamespaceObj($currentNamespace) {
      return $this->namespace[$currentNamespace];
    }

    public function setCurrentNamespace($currentNamespace) {
      $this->currentNamespace = $currentNamespace;
    }

    public function getCurrentNamespace() {
      return $this->currentNamespace;
    }

    public function setNamespace($namespace) {
      $this->namespace = $namespace;
    }

    public function getNamespace() {
      return $this->namespace;
    }


  }
?>