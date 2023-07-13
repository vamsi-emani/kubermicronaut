## Emulation of Pod restart 

A containerized micronaut application that exposes a /filecopy endpoint, when triggered multiple times 
incrementally starves the pod of its resources (memory) and eventually makes the pod restart itself. 
Plugin this container and add it to init containers of any other applications/services that are 
containerised to study pod restart behavior. 
