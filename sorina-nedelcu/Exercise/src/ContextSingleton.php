<?

  class ContextSingleton  {

    // singleton instance
    private static $instance = null;
    private $namespace = array(); // array of NamespaceObject
    private $currentNamespace;
    //private $namespace;
  	private $properties = array();

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

    public function addNamespace($namespaceObj) {
      $this->namespace[] = $namespaceObj;
    }

    public function setCurrentNamespace($currentNamespace) {
      $this->currentNamespace = $currentNamespace;
    }

    public function getCurrentNamespace() {
      return $this->currentNamespace;
    }
  }
?>