import docker.registry.*
import docker.registry.acl.AccessLevel
import docker.registry.web.TrustAnySSL
import org.springframework.beans.factory.annotation.Value

class BootStrap {
  def restService

  @Value('${ssl.trustAny}')
  boolean trustAny

  def authService

  def init = { servletContext ->

    //initializing auth

    def user = new User(username: 'test', password: 'testPassword').save(failOnError: true)
    def role = new Role('read-all').save(failOnError: true)
    def acl = new AccessControl(name: 'hello', ip: '', level: AccessLevel.PULL).save(failOnError: true)
    UserRole.create(user, role, true)
    RoleAccess.create(role, acl)

    log.info authService.login("test", "testPassword")

    if (System.env.TRUST_ANY_SSL == 'true') {
      log.info "Trusting any SSL certificate"
      TrustAnySSL.init()
    }
    restService.init()

  }
  def destroy = {
  }
}
