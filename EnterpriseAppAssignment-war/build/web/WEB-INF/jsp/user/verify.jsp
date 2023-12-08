<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@page
contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="t"
tagdir="/WEB-INF/tags/layouts/" %>

<t:main_layout>
  <jsp:attribute name="title"> Verify Email </jsp:attribute>
  <jsp:attribute name="styles">
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/resources/css/user/verify.css"
    />
  </jsp:attribute>
  <jsp:body>
    <div class="flx-all-center">
      <div class="verify-container">
        <h2 class="title">
            <svg
              width="30px"
              fill="#008000"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              x="0px"
              y="0px"
              viewBox="0 0 24 24"
              style="enable-background: new 0 0 24 24"
              xml:space="preserve"
            >
              <g id="Done">
                <g>
                  <g>
                    <g>
                      <g>
                        <g>
                          <g>
                            <g>
                              <path
                                d="M18.5,24c-3.033,0-5.5-2.467-5.5-5.5s2.467-5.5,5.5-5.5s5.5,2.467,5.5,5.5S21.533,24,18.5,24z M18.5,14
                                        c-2.481,0-4.5,2.019-4.5,4.5s2.019,4.5,4.5,4.5s4.5-2.019,4.5-4.5S20.981,14,18.5,14z"
                              />
                            </g>
                          </g>
                          <g>
                            <path
                              d="M17.5,21c-0.128,0-0.256-0.049-0.354-0.146l-2-2c-0.195-0.195-0.195-0.512,0-0.707s0.512-0.195,0.707,0l1.646,1.646
                                    l3.646-3.646c0.195-0.195,0.512-0.195,0.707,0s0.195,0.512,0,0.707l-4,4C17.756,20.951,17.628,21,17.5,21z"
                            />
                          </g>
                        </g>
                      </g>
                    </g>
                  </g>
                  <g>
                    <path
                      d="M0.5,24c-0.132,0-0.26-0.053-0.354-0.146S0,23.633,0,23.5c0-3.123,1.066-4.907,3.258-5.455l3.83-0.958l0.348-1.393
                    c-0.966-0.998-1.632-2.347-1.839-3.722c-0.625-0.122-1.121-0.634-1.203-1.287l-0.25-2C4.091,8.259,4.224,7.83,4.508,7.507
                    C4.69,7.301,4.924,7.151,5.181,7.07C5.109,6.46,5,5.449,5,5c0-1.93,0.58-3.909,4.84-3.998C11.332,0,12.935,0,13.63,0
                    c1.764,0,3.038,0.581,3.788,1.726c0.976,1.49,0.509,2.517-0.054,3.116L16.99,5.217L16.898,7.1c0.229,0.087,0.436,0.229,0.6,0.417
                    c0.28,0.321,0.41,0.747,0.357,1.169l-0.25,2c-0.076,0.612-0.518,1.102-1.089,1.26c-0.005,0.033-0.01,0.066-0.016,0.1
                    c-0.045,0.272-0.305,0.457-0.574,0.412c-0.272-0.044-0.457-0.302-0.412-0.574c0.023-0.141,0.044-0.282,0.056-0.423
                    C15.59,11.201,15.808,11,16.068,11c0.3,0,0.514-0.188,0.545-0.438l0.25-2c0.018-0.143-0.024-0.28-0.119-0.388
                    c-0.095-0.109-0.227-0.17-0.372-0.171c-0.136-0.001-0.265-0.058-0.358-0.157c-0.093-0.099-0.142-0.231-0.136-0.367L16,4.976
                    c0.006-0.124,0.058-0.241,0.146-0.329l0.5-0.5c0.313-0.333,0.599-0.858-0.065-1.873C16.028,1.429,15.036,1,13.63,1
                    c-1.025,0-2.223,0.108-3.339,0.907C10.208,1.966,10.107,1.999,10.005,2C6.306,2.037,6,3.44,6,5c0,0.489,0.168,1.925,0.232,2.438
                    C6.25,7.58,6.206,7.723,6.111,7.831C6.017,7.938,5.88,8,5.736,8H5.633C5.487,8,5.354,8.06,5.258,8.169
                    C5.162,8.278,5.119,8.417,5.137,8.562l0.25,2C5.418,10.812,5.631,11,5.883,11h0.163c0.261,0,0.478,0.2,0.499,0.46
                    c0.11,1.378,0.778,2.767,1.788,3.714c0.132,0.124,0.188,0.31,0.143,0.486l-0.49,1.961C7.94,17.8,7.8,17.94,7.621,17.985
                    L3.5,19.015C1.906,19.414,1.107,20.685,1.01,23l12.594-0.001c0.276,0,0.5,0.224,0.5,0.5c0,0.276-0.224,0.5-0.5,0.5L0.5,24z"
                    />
                  </g>
                </g>
              </g>
            </svg>
            <span>Success fully verify email</span>
          </h2>
          <p class="text">Successfully verify user account! Now, you can sign in</p>
      </div>
    </div>
  </jsp:body>
</t:main_layout>