#Difference between OpenGL GLSL and JME GLSL

# Introduction #
If you want to use GLSL shaders in other engines, so you should convert(adapt) them correctly. There are some differences between OpnGL GLSL ang JME GLSL.

OpenGL(left) and JME(right)
# Details #

<br>gl_Vertex	----------                 inPosition<br>
<br>gl_Normal	----------                 inNormal<br>
<br>gl_Color	----------                 inColor<br>
<br>gl_MultiTexCoord0	----------         inTexCoord<br>
<br>gl_ModelViewMatrix	 ----------        g_WorldViewMatrix<br>
<br>gl_ProjectionMatrix	 ----------        g_ProjectionMatrix<br>
<br>gl_ModelViewProjectionMatrix   ----------	 g_WorldViewProjectionMatrix<br>
<br>gl_NormalMatrix	----------                 g_NormalMatrix