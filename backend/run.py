import os
import platform

commands = ['build', 'image', 'run']
services = ['auth', 'comment', 'eureka', 'gateway', 'playlist', 'track']


def build_service(service):
    gradlew = '.\\gradlew.bat' if platform.system() == 'Windows' else 'gradlew'
    os.system(f'{gradlew} {service}:clean {service}:build')
    print(f'{service} is built')


def build_service_image(service):
    os.system(f'docker compose -f /{service}/docker-compose.dev.yaml --env-file=/{service}/.env build')
    print(f'{service} docker image is built')


def run_service(service, detached=True):
    suffix = ' -d' if detached else ''
    os.system(f'docker compose -f /{service}/docker-compose.dev.yaml --env-file=/{service}/.env up' + suffix)
    print(f'{service} started')


def build_all():
    for service in services:
        build_service(service)
    print('all services are built')


def build_all_images():
    os.system('docker compose -f docker-compose.dev.yaml build')
    print('docker images are built')


def run_all(detached=True):
    suffix = ' -d' if detached else ''
    os.system('docker compose -f docker-compose.dev.yaml up' + suffix)
    print('app is started')


if __name__ == "__main__":
    import sys
    args = sys.argv[1:]

    detached = sys.argv[-1] == '-d'

    actions = list(set(args) & set(commands))
    targets = list(set(args) & {'all', *services})

    if 'all' in targets:
        if 'build' in actions:
            build_all()
        if 'image' in actions:
            build_all_images()
        if 'run' in actions:
            run_all(detached)
    else:
        for target in targets:
            if 'build' in actions:
                build_service(target)
            if 'image' in actions:
                build_service_image(target)
            if 'run' in actions:
                run_service(target, detached)
